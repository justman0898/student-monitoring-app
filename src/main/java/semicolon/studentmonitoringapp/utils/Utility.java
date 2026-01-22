package semicolon.studentmonitoringapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class Utility {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomUserDetails userDetailsService;

    public LoginRequestDto extractRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
    }


}
