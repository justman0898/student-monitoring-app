package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.data.repositories.*;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminClassServiceImplTest {

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private AdminClassServiceImpl adminClassService;

    @Mock
    private AssessmentConfigRepository assessmentConfigRepository;

    @Mock
    private ParentRepository parentRepository;

    @Captor
    private ArgumentCaptor<SchoolClass> schoolClassCaptor;

    @Captor
    private ArgumentCaptor<AssessmentType> assessmentTypeArgumentCaptor;

    @Captor
    private ArgumentCaptor<AssessmentConfig> assessmentConfigArgumentCaptor;

    @Mock
    private SchoolClassMapper schoolClassMapper;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AssessmentTypeRepository assessmentTypeRepository;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    ObjectMapper objectMapper;

    @Test
    void testThatCanCreateSchoolClass() {
        CreateClassRequestDto createClassRequestDto = new CreateClassRequestDto();
        createClassRequestDto.setClassName("test");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(UUID.randomUUID());
        schoolClass.setName(createClassRequestDto.getClassName());

        Teacher teacher = new Teacher();
        teacher.setId(UUID.randomUUID());

        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setSchoolClass(Set.of(schoolClass));

        when(schoolClassMapper.toEntity(any(CreateClassRequestDto.class))).thenReturn(schoolClass);
        when(teacherRepository.findAllById(any())).thenReturn(List.of(teacher));
        when(studentRepository.findAllById(any())).thenReturn(List.of(student));
        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(schoolClass);

        adminClassService.createClass(createClassRequestDto);
        verify(schoolClassRepository).save(schoolClassCaptor.capture());

        SchoolClass captorValue = schoolClassCaptor.getValue();

        assertEquals(createClassRequestDto.getClassName(), captorValue.getName());

        assertNotNull(captorValue.getTeachers());
        assertThat(captorValue.getTeachers().size()).isEqualTo(1);
        assertThat(captorValue.getTeachers().stream().findFirst().get().getId()).isEqualTo(teacher.getId());

        assertNotNull(captorValue.getStudents());
        assertThat(captorValue.getStudents().size()).isEqualTo(1);
        assertThat(captorValue.getStudents().stream().findFirst().get().getId()).isEqualTo(student.getId());
    }

    @Test
    void testThatClassWithSameNameAndAcademicYearThrowsException() {
        CreateClassRequestDto createClassRequestDto = new CreateClassRequestDto();
        createClassRequestDto.setClassName("test");
        createClassRequestDto.setAcademicYear("test");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(createClassRequestDto.getClassName());
        schoolClass.setAcademicYear(createClassRequestDto.getAcademicYear());

        when(schoolClassRepository.existsByNameIgnoreCaseAndAcademicYear(any(), any())).thenReturn(true);
        when(schoolClassMapper.toEntity(any(CreateClassRequestDto.class))).thenReturn(schoolClass);

        assertThrows(SchoolClassDuplicateException.class, ()-> adminClassService.createClass(createClassRequestDto));
    }

    @Test
    void testThatCanGetAllSchoolClasses() {
        ClassResponseDto classResponseDto = new ClassResponseDto();
        classResponseDto.setId(UUID.randomUUID());
        when(schoolClassRepository.findAll()).thenReturn(List.of(new SchoolClass()));
        when(schoolClassMapper.toDto(any(SchoolClass.class))).thenReturn(classResponseDto);
        List<ClassResponseDto> classes = adminClassService.findAllSchoolClasses();
        assertNotNull(classes);
    }

    @Test
    void testThatCanUpdateSchoolClassToAddTeacher() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("test");
        schoolClass.setId(UUID.randomUUID());

        UUID teacherId_1 = UUID.randomUUID();
        UUID teacherId_2 = UUID.randomUUID();

        Teacher teacher_1 = new Teacher();
        teacher_1.setId(teacherId_1);

        Teacher teacher_2 = new Teacher();
        teacher_2.setId(teacherId_2);

        when(schoolClassRepository.findById(any())).thenReturn(Optional.of(schoolClass));
        when(teacherRepository.findAllById(any())).thenReturn(List.of(teacher_1,  teacher_2));

        SchoolClassPatchRequestDto patchRequestDto = new SchoolClassPatchRequestDto();
        patchRequestDto.setTeachers(Set.of(teacherId_1, teacherId_2));
        adminClassService.updateClass(schoolClass.getId() ,patchRequestDto);

        verify(schoolClassRepository).save(schoolClassCaptor.capture());
        SchoolClass captorValue = schoolClassCaptor.getValue();

        assertThat(captorValue.getTeachers().size()).isEqualTo(2);

    }

    @Test
    void testThatThrowsExceptionWhenSchoolClassNotFound(){
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("test");
        schoolClass.setId(UUID.randomUUID());

        UUID teacherId_1 = UUID.randomUUID();
        UUID teacherId_2 = UUID.randomUUID();

        SchoolClassPatchRequestDto patchRequestDto = new SchoolClassPatchRequestDto();
        patchRequestDto.setTeachers(Set.of(teacherId_1, teacherId_2));

        when(schoolClassRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> adminClassService.updateClass(schoolClass.getId() ,patchRequestDto));

    }

    @Test
    void testThatCanAddStudents(){
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("test");
        schoolClass.setId(UUID.randomUUID());

        UUID teacherId_1 = UUID.randomUUID();
        UUID teacherId_2 = UUID.randomUUID();

        Teacher teacher_1 = new Teacher();
        teacher_1.setId(teacherId_1);

        Teacher teacher_2 = new Teacher();
        teacher_2.setId(teacherId_2);

        Set<SchoolClass> schoolClasses = new HashSet<>();
        schoolClasses.add(schoolClass);

        Student student_1 = new Student();
        student_1.setId(UUID.randomUUID());
        student_1.setSchoolClass(schoolClasses);


        Student student_2 = new Student();
        student_2.setId(UUID.randomUUID());
        student_2.setSchoolClass(schoolClasses);

        when(schoolClassRepository.findById(any())).thenReturn(Optional.of(schoolClass));
        when(teacherRepository.findAllById(any())).thenReturn(List.of(teacher_1,  teacher_2));
        when(studentRepository.findAllById(any())).thenReturn(List.of(student_1, student_2));

        SchoolClassPatchRequestDto patchRequestDto = new SchoolClassPatchRequestDto();
        patchRequestDto.setTeachers(Set.of(teacherId_1, teacherId_2));
        patchRequestDto.setStudents(Set.of(teacherId_1, teacherId_2));

        adminClassService.updateClass(UUID.randomUUID(),patchRequestDto);
        verify(schoolClassRepository).save(schoolClassCaptor.capture());
        SchoolClass captorValue = schoolClassCaptor.getValue();

        assertThat(captorValue.getStudents().size()).isEqualTo(2);
    }

    @Test
    void testThatCanRemoveTeacherFromClass(){
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("test");
        schoolClass.setId(UUID.randomUUID());

        UUID teacherId_1 = UUID.randomUUID();
        UUID teacherId_2 = UUID.randomUUID();

        Teacher teacher_1 = new Teacher();
        teacher_1.setId(teacherId_1);

        Teacher teacher_2 = new Teacher();
        teacher_2.setId(teacherId_2);

        Set<Teacher> teachers = new HashSet<>();
        teachers.add(teacher_1);
        teachers.add(teacher_2);

        schoolClass.setTeachers(teachers);

        when(schoolClassRepository.findById(any())).thenReturn(Optional.of(schoolClass));
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher_1));

        adminClassService.removeTeacherFromClass(schoolClass.getId(), teacher_1.getId());

        verify(schoolClassRepository).save(schoolClassCaptor.capture());

        assertThat(schoolClassCaptor.getValue().getTeachers().size()).isEqualTo(1);
    }

    @Test
    void testThatExceptionIsThrownWhenClassDoesNotExist(){
        when(schoolClassRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                ()-> adminClassService.removeTeacherFromClass(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testThatExceptionIsThrownWhenTeacherDoesNotExist(){
        when(schoolClassRepository.findById(any())).thenReturn(Optional.of(new SchoolClass()));
        when(teacherRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                ()-> adminClassService.removeTeacherFromClass(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testThatThatDeactivateClass(){

        when(schoolClassRepository.findById(any())).thenReturn(Optional.of(new SchoolClass()));

        adminClassService.deactivateClass(UUID.randomUUID());
        verify(schoolClassRepository).save(schoolClassCaptor.capture());

        assertThat(schoolClassCaptor.getValue().getIsActive()).isFalse();
    }

    @Test
    void testThatCanCreateParentsProfiles(){

        Student student = new Student();
        student.setId(UUID.randomUUID());

        CreateParentRequestDto createParentRequestDto = new CreateParentRequestDto();
        createParentRequestDto.setFirstName("test");
        createParentRequestDto.setStudentIds(List.of(student.getId()));

        Parent parent_1 = new Parent();
        parent_1.setId(UUID.randomUUID());
        parent_1.setFirstName("test");
        parent_1.setStudents(Set.of(student));

        when(schoolClassMapper.toEntity(any(CreateParentRequestDto.class))).thenReturn(parent_1);
        when(studentRepository.findAllById(any())).thenReturn(List.of(student));
        when(parentRepository.save(any(Parent.class))).thenReturn(parent_1);

        adminClassService.createParentProfile(createParentRequestDto);
        verify(parentRepository).save(parentArgumentCaptor.capture());

        assertThat(parentArgumentCaptor.getValue()).isNotNull();
        assertThat(parentArgumentCaptor.getValue().getFirstName()).isEqualTo(createParentRequestDto.getFirstName());
        assertThat(parentArgumentCaptor.getValue().getStudents().size()).isEqualTo(1);

    }

    @Test
    void testThatCanCreateAssessmentTypes(){
        CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto = new CreateAssessmentTypeRequestDto();
        createAssessmentTypeRequestDto.setCode("EXAM");
        createAssessmentTypeRequestDto.setDescription("test");

        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setId(UUID.randomUUID());
        assessmentType.setCode(createAssessmentTypeRequestDto.getCode());
        assessmentType.setDescription(createAssessmentTypeRequestDto.getDescription());

        when(schoolClassMapper.toEntity(any(CreateAssessmentTypeRequestDto.class))).thenReturn(assessmentType);
        when(assessmentTypeRepository.save(any(AssessmentType.class)))
                .thenReturn(assessmentType);

        adminClassService.createAssessmentType(createAssessmentTypeRequestDto);
        verify(assessmentTypeRepository).save(assessmentTypeArgumentCaptor.capture());

        assertThat(assessmentTypeArgumentCaptor.getValue().getCode()).isEqualTo(createAssessmentTypeRequestDto.getCode());
        assertThat(assessmentTypeArgumentCaptor.getValue().getDescription()).isEqualTo(createAssessmentTypeRequestDto.getDescription());

    }

    @Test
    void testThatCanCreateAssessmentConfig(){
        CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto = new CreateAssessmentConfigRequestDto();
        createAssessmentConfigRequestDto.setAssessmentTypeId(UUID.randomUUID());
        createAssessmentConfigRequestDto.setSubjectId(UUID.randomUUID());
        createAssessmentConfigRequestDto.setSchoolClassId(UUID.randomUUID());

        Subject subject = new Subject();
        subject.setId(createAssessmentConfigRequestDto.getSubjectId());

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(createAssessmentConfigRequestDto.getSchoolClassId());

        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setId(createAssessmentConfigRequestDto.getAssessmentTypeId());

        AssessmentConfig assessmentConfig = new AssessmentConfig();
        assessmentConfig.setId(UUID.randomUUID());



        when(schoolClassMapper.toEntity(any(CreateAssessmentConfigRequestDto.class)))
                .thenReturn(assessmentConfig);
        when(assessmentTypeRepository.findById(any()))
                .thenReturn(Optional.of(assessmentType));
        when(schoolClassRepository.findById(any()))
                .thenReturn(Optional.of(schoolClass));
        when(subjectRepository.findById(any()))
                .thenReturn(Optional.of(subject));
        when(assessmentConfigRepository.save(any(AssessmentConfig.class)))
                .thenReturn(assessmentConfig);


        adminClassService.createAssessmentConfig(createAssessmentConfigRequestDto);

        verify(assessmentConfigRepository).save(assessmentConfigArgumentCaptor.capture());
        assertThat(assessmentConfigArgumentCaptor.getValue()
                .getAssessmentType().getId())
                .isEqualTo(createAssessmentConfigRequestDto.getAssessmentTypeId());

        assertThat(assessmentConfigArgumentCaptor.getValue()
                .getSchoolClass().getId())
                .isEqualTo(createAssessmentConfigRequestDto.getSchoolClassId());

        assertThat(assessmentConfigArgumentCaptor.getValue()
                .getSubject().getId())
                .isEqualTo(createAssessmentConfigRequestDto.getSubjectId());

    }

    @Test
    void testThatExceptionIsThrown_AssessmentIdInvalid_SubjectIdInvalid_SchoolClassIdInvalid(){
        AssessmentConfig assessmentConfig = new AssessmentConfig();
        assessmentConfig.setId(UUID.randomUUID());

        when(schoolClassMapper.toEntity(any(CreateAssessmentConfigRequestDto.class)))
                .thenReturn(assessmentConfig);
        when(assessmentTypeRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                ()-> adminClassService.createAssessmentConfig(new CreateAssessmentConfigRequestDto()));


    }



}