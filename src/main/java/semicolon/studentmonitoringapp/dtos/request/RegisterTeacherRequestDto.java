package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Subject;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;

import java.util.UUID;

@Getter
@Setter
public class RegisterTeacherRequestDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @ExistsInDb(
            entity = Teacher.class,
            field = "email",
            message = "email already exists"
    )
    private String email;

    @NotNull
    @ExistsInDb(
            entity = Teacher.class,
            field = "phone",
            message = "phone already exists"
    )
    private String phone;

    @NotNull
    private String address;

    private UUID classId;


}
