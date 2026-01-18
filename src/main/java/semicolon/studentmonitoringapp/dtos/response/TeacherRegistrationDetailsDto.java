package semicolon.studentmonitoringapp.dtos.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRegistrationDetailsDto {

    private String email;
    private String generatedPassword;
}
