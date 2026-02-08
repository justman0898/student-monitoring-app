package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import semicolon.studentmonitoringapp.data.models.Gender;
import semicolon.studentmonitoringapp.data.models.SchoolClass;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.data.repositories.SchoolClassRepository;
import semicolon.studentmonitoringapp.data.repositories.TeacherRepository;
import semicolon.studentmonitoringapp.dtos.request.RegisterEventDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.RegistrationDetailsDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import semicolon.studentmonitoringapp.utils.messaging.RegisteredEventPublisher;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTeacherServiceImplTest {

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    SchoolClassMapper mapper;

    @Mock
    Clock clock;

    @Mock
    SchoolClassRepository schoolClassRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AdminTeacherServiceImpl adminTeacherService;

    @Captor
    ArgumentCaptor<Teacher> teacherCaptor;

    @Mock
    RegisteredEventPublisher teacherRegisteredEvent;


    @Test
    void testThatCanRegisterTeacher() {

        RegisterTeacherRequestDto registerRequestDto = new RegisterTeacherRequestDto();
        registerRequestDto.setFirstName("test");
        registerRequestDto.setClassIds(List.of(UUID.randomUUID()));
        registerRequestDto.setGender(Gender.FEMALE);

        SchoolClass scc = new SchoolClass();
        scc.setId(registerRequestDto.getClassIds().get(0));

        Teacher teacher = new Teacher();
        teacher.setFirstName(registerRequestDto.getFirstName());
        teacher.setGender(registerRequestDto.getGender());


        when(teacherRepository.save(any()))
                .thenAnswer(i-> i.getArgument(0));
        when(mapper.toEntity(any(RegisterTeacherRequestDto.class)))
                .thenReturn(teacher);
        when(passwordEncoder.encode(any()))
                .thenReturn("password");
        when(schoolClassRepository.findAllById(any()))
                .thenReturn(List.of(scc));
        when(mapper.teacherToRegisterEventDto(any()))
                .thenReturn(new RegisterEventDto());

        RegistrationDetailsDto dto = adminTeacherService.registerTeacher(registerRequestDto);

        verify(teacherRepository).save(teacherCaptor.capture());

        assertThat(dto).isNotNull();
        assertThat(teacherCaptor.getValue()
                .getGeneratedPassword())
                .isEqualTo(teacher.getGeneratedPassword());

        assertThat(teacherCaptor
                .getValue()
                .getSchoolClasses()
        ).isNotNull();

        assertThat(teacherCaptor
                .getValue()
                .getSchoolClasses()
                .size()
        ).isEqualTo(1);

    }

    @Test
    void testThatCanDeactivateTeacher(){
        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setId(UUID.randomUUID());


        when(teacherRepository.findById(any()))
                .thenReturn(Optional.of(teacher));

        UUID removedTeacher = adminTeacherService.removeTeacher(UUID.randomUUID());

        assertThat(removedTeacher).isNotNull();
        assertThat(removedTeacher).isEqualTo(teacher.getId());
        assertThat(teacher.getIsActive()).isFalse();

    }

    @Test
    void testThatExceptionIsThrownWhenTeacherNotFoundDuringRemoval(){

        when(teacherRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NotFoundException.class,
                ()-> adminTeacherService.removeTeacher(UUID.randomUUID()));

    }


}