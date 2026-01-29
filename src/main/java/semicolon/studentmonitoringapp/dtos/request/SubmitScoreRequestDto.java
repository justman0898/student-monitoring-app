package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubmitScoreRequestDto {

    @NotNull
    private UUID studentId;

    @NotNull
    private UUID teacherId;

    @NotNull
    private UUID assessmentConfigId;

    @PositiveOrZero
    @NotNull
    private Integer score;





}
