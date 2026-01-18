package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;

public class CreateAssessmentTypeRequestDto {

    @NotNull
    private String code;
}
