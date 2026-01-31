package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.User;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

@Getter
@Setter
public class RegisterUserRequestDto {

    @Email(message = "Invalid email address")
    @NotBlank(message = "Enter an email")
    @ExistsInDb(entity = User.class,
            field = "email")
    @Trimmed
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter and one special character"
    )
    private String password;

}
