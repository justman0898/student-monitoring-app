package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Getter
@Setter
@ToString
public class UpdateScoreRequestDto {
    @NotNull
    private UUID id;
    @NotNull
    @PositiveOrZero
    private Integer score;
}
