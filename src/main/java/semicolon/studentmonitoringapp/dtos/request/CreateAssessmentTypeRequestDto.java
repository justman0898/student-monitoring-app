package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.AssessmentType;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;

@Getter
@Setter
public class CreateAssessmentTypeRequestDto {

    @NotNull
    @ExistsInDb(entity = AssessmentType.class,
    field = "code", message = "Assessment Type already exists"
    )
    private String code;

    private String description;
}
