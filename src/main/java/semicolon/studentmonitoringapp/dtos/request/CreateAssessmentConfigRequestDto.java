package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateAssessmentConfigRequestDto {


    private UUID subjectId;
    private UUID schoolClassId;
    private UUID assessmentTypeId;

    @NotNull
    private Integer maxScore;

    private Integer weight;
    @NotBlank
    private String academicYear;
}
