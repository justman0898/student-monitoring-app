package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.security.UserPrincipal;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private CustomUserDetails customUserDetails;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public Boolean authenticate(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        try {
            UserPrincipal user = customUserDetails.loadUserByUsername(loginRequestDto.getUsername());
            log.info(user.toString());
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                return false;
            }
            String token = jwtProvider.generateToken(user);
            log.info(token);
            httpServletResponse.setHeader("Authorization", "Bearer " + token);
            return true;
        }catch (UsernameNotFoundException e){
            log.info("Error: {}",  e.getMessage());
            return false;
        }
    }
}
