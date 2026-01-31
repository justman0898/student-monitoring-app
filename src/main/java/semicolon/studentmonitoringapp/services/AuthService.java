package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;

import java.util.UUID;

public interface AuthService {

    Boolean authenticate(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse);
    UUID register(RegisterUserRequestDto registerUserRequestDto);
}
