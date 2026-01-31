package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;

import java.util.UUID;

public interface TeacherAssessmentController {


    ResponseEntity<IdResponse> submitScore(SubmitScoreRequestDto submitScoreRequestDto);
    ResponseEntity<IdResponse> updateScore(UpdateScoreRequestDto requestDto);
    ResponseEntity<?> deleteScore(UUID scoreId);
    ResponseEntity<IdResponse> addComment(CreateCommentRequestDto commentRequest);
    void getAcademicHistory(UUID studentId);

}
