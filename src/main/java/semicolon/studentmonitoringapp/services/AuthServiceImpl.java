package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semicolon.studentmonitoringapp.data.models.Role;
import semicolon.studentmonitoringapp.data.models.User;
import semicolon.studentmonitoringapp.data.repositories.UserRepository;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.security.UserPrincipal;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        log.info("Saved: {}", user);

    }


}
