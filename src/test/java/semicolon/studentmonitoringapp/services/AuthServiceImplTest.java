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
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

        UUID createdUser = authService.registerAdmin(requestDto);

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

    @Test
    void testThatCanRegisterUser(){

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("test");
        requestDto.setPassword("test");
        requestDto.setRoles(Set.of(Role.PARENT));

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setId(UUID.randomUUID());


        when(mapper.toEntity(any(UserRequestDto.class)))
                .thenReturn(user);
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        authService.registerUser(requestDto);


        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue()
                .getEmail())
                .isEqualTo(requestDto.getEmail());

        assertThat(userCaptor.getValue()
                .getRoles())
                .isEqualTo(user.getRoles());
    }

    @Test
    void testThatNewRoleIsAddedWhenUserAlreadyExist(){
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setId(UUID.randomUUID());
        requestDto.setEmail("test");
        requestDto.setPassword("test");
        requestDto.setRoles(Set.of(Role.PARENT));

        User userFromMapper =  new User();
        userFromMapper.setId(requestDto.getId());
        userFromMapper.setEmail(requestDto.getEmail());
        userFromMapper.setPassword(requestDto.getPassword());
        userFromMapper.setRoles(Set.of(Role.PARENT));

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setId(requestDto.getId());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setRoles(roles);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        authService.registerUser(requestDto);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue()
                .getRoles()
                .containsAll(Set.of(Role.ADMIN,  Role.PARENT)))
                .isTrue();




    }















}