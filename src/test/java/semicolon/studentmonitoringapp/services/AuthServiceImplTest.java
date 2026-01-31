package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import semicolon.studentmonitoringapp.data.models.Role;
import semicolon.studentmonitoringapp.data.models.User;
import semicolon.studentmonitoringapp.data.repositories.UserRepository;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import javax.management.relation.RoleStatus;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private SchoolClassMapper mapper;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testThatCanRegisterAdminUser(){
        RegisterUserRequestDto requestDto = new RegisterUserRequestDto();
        requestDto.setEmail("test");
        requestDto.setPassword("test");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        when(mapper.toEntity(requestDto)).thenReturn(user);

        UUID createdUser = authService.register(requestDto);

        assertThat(createdUser).isNotNull();

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue()
                .getEmail())
                .isEqualTo(requestDto.getEmail());

        assertThat(userCaptor.getValue()
                .getId())
                .isEqualTo(createdUser);

        assertThat(userCaptor.getValue()
                .getRoles().stream().findFirst().get())
                .isEqualTo(Role.ADMIN);



    }

}