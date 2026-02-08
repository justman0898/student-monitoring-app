package semicolon.studentmonitoringapp.dtos.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDetailsDto {

    private String email;
    private String generatedPassword;
}
