package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class TeacherResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Instant createdAt;

    private Set<ClassResponseDto> schoolClasses  = new HashSet<>();
}
