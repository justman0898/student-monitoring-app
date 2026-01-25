package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.AssessmentType;

import java.util.UUID;

@Getter
@Setter
public class AssessmentConfigResponseDto {

    private UUID id;
    private SubjectResponseDto subject;
    private ClassResponseDto schoolClass;
    private CreateAssessmentTypeResponseDto assessmentType;
    private Integer maxScore;
    private Integer weight;
    private String academicYear;


}
