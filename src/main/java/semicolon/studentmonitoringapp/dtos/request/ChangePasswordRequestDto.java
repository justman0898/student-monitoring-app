package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChangePasswordRequestDto {
    private UUID id;
    private String oldPassword;
    @NotNull
    private String newPassword;
    private String otp;
}
