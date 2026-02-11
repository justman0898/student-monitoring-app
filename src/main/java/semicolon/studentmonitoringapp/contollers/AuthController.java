package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.studentmonitoringapp.dtos.request.ChangePasswordRequestDto;
import semicolon.studentmonitoringapp.dtos.request.PasswordResetRequest;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.services.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<IdResponse> register(@Valid @RequestBody
                                                   RegisterUserRequestDto registerUserRequestDto) {
        UUID  userId = authService.registerAdmin(registerUserRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(userId));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestPasswordReset(@Valid  @RequestBody PasswordResetRequest resetRequest){
        authService.requestPasswordReset(resetRequest.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(authService.changePassword(changePasswordRequestDto));
    }
}
