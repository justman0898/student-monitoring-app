package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Gender;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
public class StudentResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;

    private Instant createdAt;




    private List<CreateParentResponseDto> parents;
}
