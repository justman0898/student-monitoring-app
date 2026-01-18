package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssessmentTypeResponseDto {
    private UUID id;
    private String code;
}
