package semicolon.studentmonitoringapp.dtos.response;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TeacherRegistrationDetailsDto {

    private String email;
    private String generatedPassword;
    private UUID teacherId;
}
