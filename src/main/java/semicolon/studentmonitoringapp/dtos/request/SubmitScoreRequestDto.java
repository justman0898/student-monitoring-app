package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubmitScoreRequestDto {

    @NotNull(message = "please enter a student Id")
    private UUID studentId;

    @NotNull(message = "please enter a teacher Id")
    private UUID teacherId;

    @NotNull(message = "please enter a config Id")
    private UUID assessmentConfigId;

    @PositiveOrZero(message = "enter a valid score")
    @NotNull(message = "score cannot be blank")
    private Integer score;





}
