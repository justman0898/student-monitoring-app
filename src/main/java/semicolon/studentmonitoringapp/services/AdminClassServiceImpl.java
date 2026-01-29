package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.data.repositories.*;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.*;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

import java.util.*;

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


    @Override
    public UUID createClass(CreateClassRequestDto classRequestDto) {
        SchoolClass schoolClass = schoolClassMapper.toEntity(classRequestDto);
        checkForDuplicate(classRequestDto, schoolClass);
        List<Teacher> teachers = teacherRepository.findAllById(classRequestDto.getTeacherIds());
        List<Student> students = studentRepository.findAllById(classRequestDto.getStudentIds());
        schoolClass.setTeachers(new HashSet<>(teachers));
        schoolClass.setStudents(new HashSet<>(students));
        SchoolClass saved = classRepository.save(schoolClass);
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
        getAllTeachersById(classPatchDto.getTeachers()).forEach(schoolClass::addTeacher);
        getAllStudentsById(classPatchDto.getStudents()).forEach(schoolClass::addStudent);
        classRepository.save(schoolClass);
    }

    @Override
    @Transactional
    public void removeTeacherFromClass(UUID classId, UUID teacherId) {
        SchoolClass schoolClass = getSchoolClass(classId);
        Teacher teacher = getTeacher(teacherId);
        schoolClass.removeTeacher(teacher);
        classRepository.save(schoolClass);
    }

    @Override
    public void deactivateClass(UUID classId) {
        SchoolClass schoolClass = getSchoolClass(classId);
        schoolClass.setIsActive(false);
        classRepository.save(schoolClass);
    }

    @Override
    @Transactional
    public UUID createParentProfile(CreateParentRequestDto createParentRequestDto) {
        Parent parent = schoolClassMapper.toEntity(createParentRequestDto);
        List<Student> students = studentRepository.findAllById(createParentRequestDto.getStudentIds());
        parent.setStudents(new HashSet<>(students));
        Parent saved = parentRepository.save(parent);
        return saved.getId();
    }

    @Override
    public UUID createAssessmentType(CreateAssessmentTypeRequestDto assessmentTypeRequestDto) {
        AssessmentType assessmentType = schoolClassMapper.toEntity(assessmentTypeRequestDto);
        assessmentType.setCode(assessmentTypeRequestDto.getCode().toUpperCase().trim());
        AssessmentType saved = assessmentTypeRepository.save(assessmentType);
        return saved.getId();
    }

    @Override
    public UUID createAssessmentConfig(CreateAssessmentConfigRequestDto assessmentConfigRequestDto) {
        AssessmentConfig assessmentConfig = mapAssessmentConfigToEntity(assessmentConfigRequestDto);
        AssessmentConfig saved = assessmentConfigRepository.save(assessmentConfig);
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
