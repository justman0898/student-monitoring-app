package semicolon.studentmonitoringapp.dtos.request;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserRequestDto {
    private UUID id;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
