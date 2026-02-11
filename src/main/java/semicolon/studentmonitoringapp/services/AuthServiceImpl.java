package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.Role;
import semicolon.studentmonitoringapp.data.models.User;
import semicolon.studentmonitoringapp.data.repositories.UserRepository;
import semicolon.studentmonitoringapp.dtos.request.ChangePasswordRequestDto;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.OtpException;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.security.UserPrincipal;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomUserDetails customUserDetails;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final SchoolClassMapper schoolClassMapper;
    private final SecureRandom secureRandom = new SecureRandom();
    private final StringRedisTemplate stringRedisTemplate;
    private final static int OTP_TTL_MINUTES = 5;
    private final static int MAX_ATTEMPTS = 3;

    public Boolean authenticate(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        try {
            UserPrincipal user = customUserDetails.loadUserByUsername(loginRequestDto.getUsername());
            log.info("Loaded user: {}",
                    objectMapper.writeValueAsString(user));
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                log.error("Invalid Credentials");
                return false;
            }
            String token = jwtProvider.generateToken(user);

            httpServletResponse.setHeader("Authorization", "Bearer " + token);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
        }catch (UsernameNotFoundException e){
            log.info("Error: {}",  e.getMessage());
            return false;
        }
    }

    @Override
    public UUID registerAdmin(RegisterUserRequestDto registerUserRequestDto) {
        User user = schoolClassMapper.toEntity(registerUserRequestDto);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        user.setRoles(roles);
        userRepository.save(user);
        log.info("New user created: {}",
                objectMapper.writeValueAsString(user));
        return user.getId();
    }

    @Override
    public void registerUser(UserRequestDto userRequestDto) {

        Set<Role> roles = userRequestDto.getRoles();
        User user = userRepository.findByEmail(userRequestDto.getEmail())
                        .map(found-> {
                            found.getRoles().addAll(roles);
                            return found;
                        }).or(()->{
                    User newUser = schoolClassMapper.toEntity(userRequestDto);
                    return java.util.Optional.of(newUser);
                }).orElseThrow(() -> new IllegalStateException("Could not create user"));;

        userRepository.save(user);
        log.info("Saved: {}", objectMapper.writeValueAsString(user));

    }

    @Override
    @Transactional
    public String changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        validateOtp(changePasswordRequestDto);
        User user = getUser(changePasswordRequestDto);
        String hashedPassword = passwordEncoder
                .encode(changePasswordRequestDto.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "token";
    }

    private User getUser(ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userRepository.findById(changePasswordRequestDto.getId())
                        .orElseThrow(()-> new NotFoundException("user not found"));
        return user;
    }

    private void validateOtp(ChangePasswordRequestDto changePasswordRequestDto) {
        String otpKey = otpKey(changePasswordRequestDto.getId().toString());
        String attemptsKey = attemptKey(changePasswordRequestDto.getId().toString());
        String storedHash = stringRedisTemplate.opsForValue()
                .get(otpKey);
        String attemptsValue = stringRedisTemplate.opsForValue()
                .get(attemptsKey);

        if(storedHash == null || attemptsValue == null)
            throw new OtpException("Otp expired or invalid");

        int attempts = Integer.parseInt(attemptsValue);
        if(attempts > MAX_ATTEMPTS) {
            stringRedisTemplate.delete(otpKey);
            stringRedisTemplate.delete(attemptsKey);
            throw new OtpException("Otp Locked");
        }
        boolean isValid = passwordEncoder
                .matches(changePasswordRequestDto.getOtp(), storedHash);

        if(!isValid) {
            stringRedisTemplate.opsForValue()
                    .increment(attemptsKey);
            throw new OtpException("Invalid Otp");
        }
    }

    @Override
    public void requestPasswordReset(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {
            invalidateExistingOtp(user);
            String otp = getOtp();
            log.info("OTP reset: {} User: {}", otp,
                    objectMapper.writeValueAsString(user));
            storeOtp(user, otp);
        });

    }








    private void storeOtp(User user, String otp) {
        String otp_key = otpKey(user.getId().toString());
        String attempts_key = attemptKey(user.getId().toString());
        String hashedOtp = passwordEncoder.encode(otp);
        ValueOperations<String, String> valueOperations =
                stringRedisTemplate.opsForValue();

        valueOperations
                .set(otp_key, hashedOtp, OTP_TTL_MINUTES, TimeUnit.MINUTES);

        valueOperations
                .set(attempts_key, "0", OTP_TTL_MINUTES, TimeUnit.MINUTES);
    }

    private void invalidateExistingOtp(User user) {
        stringRedisTemplate.delete(otpKey(user.getId().toString()));
        stringRedisTemplate.delete(attemptKey(user.getId().toString()));
    }

    private static String attemptKey(String id) {
        return "otp:reset:attempt:" + id;
    }

    private static String otpKey(String id) {
        return "otp:reset:" + id;
    }


    private Boolean verifyOtp(String otp) {
        return null;
    }


    private String getOtp() {
        int random = secureRandom.nextInt(900_000) + 100_000;
        return String.valueOf(random);
    }


}
