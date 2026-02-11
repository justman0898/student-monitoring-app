package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

@Getter
@Setter
public class PasswordResetRequest {

    @Email
    @NotBlank
    @NotNull
    @Trimmed
    private String email;
}
