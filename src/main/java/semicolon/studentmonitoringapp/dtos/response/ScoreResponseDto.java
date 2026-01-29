package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ScoreResponseDto {

    private UUID id;
    private StudentResponseDto student;
    private TeacherResponseDto teacher;
    private AssessmentConfigResponseDto assessmentConfig;
    private Integer score;
    private Instant recordDate;
}
