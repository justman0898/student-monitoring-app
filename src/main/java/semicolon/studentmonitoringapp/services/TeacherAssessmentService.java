package semicolon.studentmonitoringapp.services;

import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;

import java.util.UUID;

public interface TeacherAssessmentService {

    UUID submitScore(SubmitScoreRequestDto scoreRequestDto);
    UUID updateScore(UpdateScoreRequestDto scoreRequestDto);
    void deleteScore(UUID scoreId);
    UUID addComment(CreateCommentRequestDto commentRequestDto);

}
