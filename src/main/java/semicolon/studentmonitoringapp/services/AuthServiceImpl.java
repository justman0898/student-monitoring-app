package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.security.UserPrincipal;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomUserDetails customUserDetails;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

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
            return true;
        }catch (UsernameNotFoundException e){
            log.info("Error: {}",  e.getMessage());
            return false;
        }
    }
}
