package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;

public interface AuthService {

    Boolean authenticate(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse);
}
