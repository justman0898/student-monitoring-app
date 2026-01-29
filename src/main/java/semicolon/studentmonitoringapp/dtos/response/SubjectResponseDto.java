package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class SubjectResponseDto {
    private UUID id;
    private String name;
    private String code;
}
