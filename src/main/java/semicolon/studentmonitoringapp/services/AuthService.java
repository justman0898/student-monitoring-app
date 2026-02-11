package semicolon.studentmonitoringapp.services;

import jakarta.servlet.http.HttpServletResponse;
import semicolon.studentmonitoringapp.dtos.request.ChangePasswordRequestDto;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;

import java.util.UUID;

public interface AuthService {

    Boolean authenticate(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse);
    UUID registerAdmin(RegisterUserRequestDto registerUserRequestDto);
    void registerUser(UserRequestDto userRequestDto);
    String changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    void requestPasswordReset(String email);

}
