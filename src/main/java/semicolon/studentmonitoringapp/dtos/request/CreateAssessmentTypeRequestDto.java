package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.AssessmentType;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

@Getter
@Setter
public class CreateAssessmentTypeRequestDto {

    @NotNull
    @ExistsInDb(entity = AssessmentType.class,
    field = "code", message = "Assessment Type already exists"
    )
    @Trimmed
    private String code;

    @Trimmed
    private String description;
}
