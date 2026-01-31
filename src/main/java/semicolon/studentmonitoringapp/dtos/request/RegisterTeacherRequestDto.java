package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Gender;
import semicolon.studentmonitoringapp.data.models.Subject;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RegisterTeacherRequestDto {

    @NotBlank(message = "Cannot be blank")
    private String firstName;

    @NotBlank(message = "Cannot be empty")
    private String lastName;

    @NotNull
    @ExistsInDb(
            entity = Teacher.class,
            field = "email",
            message = "email already exists"
    )
    @Email
    private String email;

    @NotNull
    @ExistsInDb(
            entity = Teacher.class,
            field = "phone",
            message = "phone already exists"
    )
    @Pattern(regexp = "^\\d{9,}$", message = "phone number cannot be less than 9")
    private String phone;


    private String address;

    private List<UUID> classIds;

    @NotNull
    private Gender gender;


}
