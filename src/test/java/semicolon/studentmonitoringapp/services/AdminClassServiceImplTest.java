package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import semicolon.studentmonitoringapp.data.models.SchoolClass;
import semicolon.studentmonitoringapp.data.models.Student;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.data.repositories.SchoolClassRepository;
import semicolon.studentmonitoringapp.data.repositories.StudentRepository;
import semicolon.studentmonitoringapp.data.repositories.TeacherRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    @Captor
    private ArgumentCaptor<SchoolClass> schoolClassCaptor;


    @Mock
    private SchoolClassMapper schoolClassMapper;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    void testThatCanCreateSchoolClass() {
        CreateClassRequestDto createClassRequestDto = new CreateClassRequestDto();
        createClassRequestDto.setClassName("test");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(createClassRequestDto.getClassName());

        Teacher teacher = new Teacher();
        teacher.setId(UUID.randomUUID());

        Student student = new Student();
        student.setId(UUID.randomUUID());

        when(schoolClassMapper.toEntity(any())).thenReturn(schoolClass);
        when(teacherRepository.findAllById(any())).thenReturn(List.of(teacher));
        when(studentRepository.findAllById(any())).thenReturn(List.of(student));

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
        when(schoolClassMapper.toEntity(any())).thenReturn(schoolClass);

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

        Student student_1 = new Student();
        student_1.setId(UUID.randomUUID());

        Student student_2 = new Student();
        student_2.setId(UUID.randomUUID());

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

}