package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;
import semicolon.studentmonitoringapp.utils.annotations.ValidAcademicYear;

import java.util.UUID;

@Getter
@Setter
public class CreateAssessmentConfigRequestDto {


    private UUID subjectId;
    private UUID schoolClassId;
    private UUID assessmentTypeId;

    @NotNull(message = "please enter a value")
    private Integer maxScore;

    private Integer weight;
    @NotBlank(message = "please enter an academic year")
    @Trimmed
    @ValidAcademicYear
    private String academicYear;
}
