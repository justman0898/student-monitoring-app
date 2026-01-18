package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Gender;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreateParentResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private Gender gender;


    private Set<StudentResponseDto> students = new HashSet<>();
}
