package semicolon.studentmonitoringapp.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import semicolon.studentmonitoringapp.data.models.Role;
import semicolon.studentmonitoringapp.data.models.User;
import semicolon.studentmonitoringapp.data.repositories.UserRepository;
import semicolon.studentmonitoringapp.dtos.request.ChangePasswordRequestDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;
import semicolon.studentmonitoringapp.exceptions.OtpException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
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

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;



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

    @Test
    void testThatCanRequestPasswordReset(){

        String email = "jay@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setId(UUID.randomUUID());
        String hashedOtp = "hashedOtp";
        Long minutes = 5L;

        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(user));
        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);
        when(passwordEncoder.encode(any()))
                .thenReturn("hashedOtp");

        authService.requestPasswordReset(email);

        String attempts_key = "otp:reset:attempt:" + user.getId();
        String otp_key = "otp:reset:" + user.getId();

        verify(stringRedisTemplate).delete(otp_key);
        verify(stringRedisTemplate).delete(attempts_key);

        verify(valueOperations).set(eq(otp_key),
                eq(hashedOtp),
                eq(minutes), eq(TimeUnit.MINUTES));

        verify(valueOperations).set(eq(attempts_key),
                eq("0"),
                eq(minutes), eq(TimeUnit.MINUTES));
    }

    @Test
    void testThatCanChangePassword(){
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setId(UUID.randomUUID());
        requestDto.setOtp("786490");
        requestDto.setNewPassword("newPassword");
        String otp_key = "otp:reset:" + requestDto.getId();
        String attempts_key = "otp:reset:attempt:" + requestDto.getId();


        User user = new User();
        user.setId(requestDto.getId());
        user.setPassword("password");
        user.setEmail("jay@gmail");

        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);
        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any()))
                .thenReturn(requestDto.getNewPassword());
        when(valueOperations.get(otp_key))
                .thenReturn("087688");
        when(valueOperations.get(attempts_key))
                .thenReturn("1");
        when(passwordEncoder.matches(requestDto.getOtp(), "087688"))
                .thenReturn(true);

        String token = authService.changePassword(requestDto);


        assertThat(token).isNotBlank();

        verify(valueOperations).get(otp_key);
        verify(valueOperations).get(attempts_key);
        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue()
                .getId())
                .isEqualTo(requestDto.getId());

        assertThat(userCaptor.getValue()
                .getPassword())
                .isEqualTo(requestDto.getNewPassword());
    }

    @Test
    void testThatThrowsExceptionForInvalidOtp(){

        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setId(UUID.randomUUID());
        requestDto.setOtp("786490");
        requestDto.setNewPassword("newPassword");
        requestDto.setOtp("605543");

        String otp_key = "otp:reset:" + requestDto.getId();

        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);
        when(valueOperations.get(otp_key))
                .thenReturn(null);

        assertThrows(OtpException.class,()->
                authService.changePassword(requestDto));
    }

    @Test
    void testThatExceptionIsThrownWhenMaximumAttemptIsExceeded(){
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setId(UUID.randomUUID());
        requestDto.setOtp("786490");
        requestDto.setNewPassword("newPassword");
        requestDto.setOtp("605543");

        String otp_key = "otp:reset:" + requestDto.getId();
        String attempts_key = "otp:reset:attempt:" + requestDto.getId();


        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);
        when(valueOperations.get(otp_key))
                .thenReturn("893211");
        when(valueOperations.get(attempts_key))
                .thenReturn("4");

        assertThrows(OtpException.class,()->
                authService.changePassword(requestDto));

        verify(stringRedisTemplate).delete(otp_key);
        verify(stringRedisTemplate).delete(attempts_key);
    }

    @Test
    void testThatThrowsExceptionWhenOtpIsIncorrect(){
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setId(UUID.randomUUID());
        requestDto.setOtp("786490");
        requestDto.setNewPassword("newPassword");
        requestDto.setOtp("605543");

        String otp_key = "otp:reset:" + requestDto.getId();
        String attempts_key = "otp:reset:attempt:" + requestDto.getId();

        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);
        when(valueOperations.get(otp_key))
                .thenReturn("893211");
        when(valueOperations.get(attempts_key))
                .thenReturn("2");
        when(passwordEncoder.matches(requestDto.getOtp(), "893211"))
                .thenReturn(false);

        assertThrows(OtpException.class,()->
                authService.changePassword(requestDto));

        verify(valueOperations).increment(attempts_key);
    }



















}