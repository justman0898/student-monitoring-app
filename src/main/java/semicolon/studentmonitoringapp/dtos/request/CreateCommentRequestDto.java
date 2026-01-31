package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class CreateCommentRequestDto {

    private UUID subjectId;
    private UUID studentId;
    @NotBlank(
            message = "please enter a text"
    )
    private String text;
    private UUID teacherId;

}
