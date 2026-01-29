package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class CreateAssessmentTypeResponseDto {

    private UUID id;
    private String code;
    private String description;

    private Instant createdAt;
}
