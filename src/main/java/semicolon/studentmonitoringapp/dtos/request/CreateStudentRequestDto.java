package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import semicolon.studentmonitoringapp.data.models.Gender;
import semicolon.studentmonitoringapp.data.models.Student;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

import java.util.UUID;

@Getter
@Setter
public class CreateStudentRequestDto {

    @NotNull
    @NotBlank
    @Trimmed
    private String firstName;
    @NotNull
    @NotBlank
    @Trimmed
    private String lastName;
    @NotNull
    @NotBlank
    @Trimmed
    @Email
    @ExistsInDb(
            entity = Student.class,
            field = "email"
    )
    private String email;
    @NotNull
    private Gender gender;

}
