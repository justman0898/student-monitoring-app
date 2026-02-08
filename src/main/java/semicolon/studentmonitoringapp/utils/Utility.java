package semicolon.studentmonitoringapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class Utility {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    private final ObjectMapper objectMapper;

    public static @NotNull String generateTeacherPassword() {
        StringBuilder sb = new StringBuilder("TCH-");
        for (int i = 0; i < 6; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    public static @NotNull String generateParentPassword() {
        StringBuilder sb = new StringBuilder("PNT-");
        for (int i = 0; i < 6; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    public LoginRequestDto extractRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
    }


}
