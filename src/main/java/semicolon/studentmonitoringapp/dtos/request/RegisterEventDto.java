package semicolon.studentmonitoringapp.dtos.request;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Role;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RegisterEventDto {
    private UUID id;
    private String email;
    private String generatedPassword;
    private Set<Role> roles;
}
