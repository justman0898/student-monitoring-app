package semicolon.studentmonitoringapp.dtos.request;

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
    private String text;
    private UUID teacherId;

}
