package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.data.repositories.*;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.*;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;
import semicolon.studentmonitoringapp.utils.Utility;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import semicolon.studentmonitoringapp.utils.messaging.RegisteredEventPublisher;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class AdminClassServiceImpl implements AdminClassService {
    private final SchoolClassRepository classRepository;
    private final SchoolClassMapper schoolClassMapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final AssessmentTypeRepository assessmentTypeRepository;
    private final AssessmentConfigRepository assessmentConfigRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;
    private final RegisteredEventPublisher registeredEventPublisher;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UUID createClass(CreateClassRequestDto classRequestDto) {
        log.info("Request(Service layer): {}", objectMapper.writeValueAsString(classRequestDto));
        SchoolClass schoolClass = schoolClassMapper.toEntity(classRequestDto);
        checkForDuplicate(classRequestDto, schoolClass);
        List<Teacher> teachers = teacherRepository.findAllById(classRequestDto.getTeacherIds());
        List<Student> students = studentRepository.findAllById(classRequestDto.getStudentIds());
        schoolClass.setTeachers(new HashSet<>(teachers));
        schoolClass.setStudents(new HashSet<>(students));
        SchoolClass saved = classRepository.save(schoolClass);
        logSaved(saved);

        return saved.getId();
    }



    @Override
    public List<ClassResponseDto> findAllSchoolClasses() {
        List<SchoolClass> classes = classRepository.findAll();
        return getClassResponseDtos(classes);
    }

    @Override
    @Transactional
    public void updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto) {
        SchoolClass schoolClass = getSchoolClass(classId);
        if(!classPatchDto.getTeachers().isEmpty())
            getAllTeachersById(classPatchDto.getTeachers())
                    .forEach(schoolClass::addTeacher);
        if(!classPatchDto.getStudents().isEmpty())
            getAllStudentsById(classPatchDto.getStudents())
                .forEach(schoolClass::addStudent);
        classRepository.save(schoolClass);
        log.info("Updated: {}", objectMapper.writeValueAsString(classPatchDto));
    }

    @Override
    @Transactional
    public void removeTeacherFromClass(UUID classId, UUID teacherId) {
        SchoolClass schoolClass = getSchoolClass(classId);
        Teacher teacher = getTeacher(teacherId);
        schoolClass.removeTeacher(teacher);
        classRepository.save(schoolClass);
        log.info("Removed Teacher :{} from class {}", 
                objectMapper.writeValueAsString(teacher), 
                objectMapper.writeValueAsString(schoolClass));
    }

    @Override
    public void deactivateClass(UUID classId) {
        SchoolClass schoolClass = getSchoolClass(classId);
        schoolClass.setIsActive(false);
        classRepository.save(schoolClass);
        log.info("Deactivated: {}", 
                objectMapper.writeValueAsString(schoolClass));
    }

    @Override
    @Transactional
    public RegistrationDetailsDto createParentProfile(CreateParentRequestDto createParentRequestDto) {
        Parent parent = schoolClassMapper.toEntity(createParentRequestDto);
        List<Student> students = studentRepository.findAllById(createParentRequestDto.getStudentIds());
        parent.setStudents(new HashSet<>(students));
        String password = Utility.generateParentPassword();
        String hashedPassword = passwordEncoder.encode(password);
        parent.setGeneratedPassword(hashedPassword);

        Parent saved = parentRepository.save(parent);

        RegisterEventDto registerEventDto = schoolClassMapper
                .parentToRegisterEventDto(saved);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.PARENT);
        registerEventDto.setRoles(roles);
        registeredEventPublisher.broadcastEvent(registerEventDto);

        RegistrationDetailsDto registrationDetailsDto = new RegistrationDetailsDto();
        registrationDetailsDto.setEmail(parent.getEmail());
        registrationDetailsDto.setGeneratedPassword(password);

        log.info("Created Parent: {}",
                objectMapper.writeValueAsString(saved));

        return registrationDetailsDto;
    }

    @Override
    public UUID createAssessmentType(CreateAssessmentTypeRequestDto assessmentTypeRequestDto) {
        AssessmentType assessmentType = schoolClassMapper.toEntity(assessmentTypeRequestDto);
        assessmentType.setCode(assessmentTypeRequestDto.getCode().toUpperCase().trim());
        AssessmentType saved = assessmentTypeRepository.save(assessmentType);
        log.info("Created AssessmentType: {}",
                objectMapper.writeValueAsString(saved));
        return saved.getId();
    }

    @Override
    public UUID createAssessmentConfig(CreateAssessmentConfigRequestDto assessmentConfigRequestDto) {
        AssessmentConfig assessmentConfig = mapAssessmentConfigToEntity(assessmentConfigRequestDto);
        AssessmentConfig saved = assessmentConfigRepository.save(assessmentConfig);
        log.info("Created Assessment Configuration: {}",
                objectMapper.writeValueAsString(saved));
        return saved.getId();
    }

    @Override
    public CreateAssessmentTypeResponseDto getAssessmentType(UUID assessmentTypeId) {
        return assessmentTypeRepository.findById(assessmentTypeId)
                .map(schoolClassMapper::toDto)
                .orElseThrow(()-> new NotFoundException("assessment type not found"));
    }

    @Override
    public List<CreateAssessmentTypeResponseDto> getAllAssessmentTypes() {
        return assessmentTypeRepository.findAll()
                .stream().map(schoolClassMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UUID updateParent(UpdateParentRequestDto updateParentRequestDto) {
        Parent parent = getParent(updateParentRequestDto);
        if(updateParentRequestDto.getPhone() != null)
            parent.setPhone(updateParentRequestDto.getPhone());

        if(updateParentRequestDto.getAddress() != null)
            parent.setAddress(updateParentRequestDto.getAddress());

        if(!updateParentRequestDto.getStudentIds().isEmpty())
            studentRepository
                .findAllById(updateParentRequestDto.getStudentIds())
                .forEach(parent::addStudent);

        parentRepository.save(parent);
        return parent.getId();
    }

    @Override
    public UUID registerStudent(CreateStudentRequestDto createStudentRequestDto) {
        Student student = schoolClassMapper.toEntity(createStudentRequestDto);
        studentRepository.save(student);
        return student.getId();
    }


    private Parent getParent(UpdateParentRequestDto updateParentRequestDto) {
        return parentRepository.findById(updateParentRequestDto.getId())
                        .orElseThrow(()-> new NotFoundException("parent not found"));
    }


    private AssessmentConfig mapAssessmentConfigToEntity(CreateAssessmentConfigRequestDto assessmentConfigRequestDto) {
        AssessmentConfig assessmentConfig = schoolClassMapper.toEntity(assessmentConfigRequestDto);
        AssessmentType assessmentType = assessmentTypeRepository.findById(assessmentConfigRequestDto.getAssessmentTypeId())
                        .orElseThrow(()-> new NotFoundException("assessment type not found"));
        assessmentConfig.setAssessmentType(assessmentType);

        SchoolClass schoolClass = classRepository.findById(assessmentConfigRequestDto.getSchoolClassId())
                        .orElseThrow(()->new NotFoundException("school class not found"));
        assessmentConfig.setSchoolClass(schoolClass);

        Subject subject = subjectRepository.findById(assessmentConfigRequestDto.getSubjectId())
                        .orElseThrow(()-> new NotFoundException("subject not found"));
        assessmentConfig.setSubject(subject);
        return assessmentConfig;
    }


    private Teacher getTeacher(UUID teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Teacher Not Found"));
    }

    private SchoolClass getSchoolClass(UUID classId) {
        return classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class Not Found"));
    }

    private void logSaved(SchoolClass saved) {
        log.info("Saved {}",objectMapper.writeValueAsString(saved));
    }

    private List<Student> getAllStudentsById(Set<UUID> studentIds) {
        return studentRepository.findAllById(studentIds);
    }

    private List<Teacher> getAllTeachersById(Set<UUID> teacherIds) {
        return teacherRepository.findAllById(teacherIds);
    }


    private List<ClassResponseDto> getClassResponseDtos(List<SchoolClass> classes) {
        return classes.stream()
                .map(this::populateClassResponseDto)
                .toList();
    }

    private ClassResponseDto populateClassResponseDto(SchoolClass schoolClass) {
        ClassResponseDto responseDto = schoolClassMapper.toDto(schoolClass);
        List<TeacherResponseDto> teachers = schoolClass.getTeachers().stream().map(schoolClassMapper::toDto).toList();
        List<StudentResponseDto> students = schoolClass.getStudents().stream().map(schoolClassMapper::toDto).toList();
        responseDto.setTeachers(teachers);
        responseDto.setStudents(students);
        return responseDto;
    }


    private void checkForDuplicate(CreateClassRequestDto classRequestDto, SchoolClass schoolClass) {
        if(classRepository.existsByNameIgnoreCaseAndAcademicYear(schoolClass.getName(), schoolClass.getAcademicYear())) {
            throw new SchoolClassDuplicateException(classRequestDto);
        }
    }
}
