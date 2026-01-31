package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.data.repositories.CommentRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.services.TeacherAssessmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@AllArgsConstructor
public class TeacherAssessmentControllerImpl implements TeacherAssessmentController {

    private final TeacherAssessmentService teacherAssessmentService;
    private final CommentRepository commentRepository;

    @Override
    @PostMapping("/submit")
    public ResponseEntity<IdResponse> submitScore(@Valid @RequestBody
                                SubmitScoreRequestDto submitScoreRequestDto) {
        UUID scoreId = teacherAssessmentService.submitScore(submitScoreRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(scoreId));
    }

    @Override
    @PatchMapping("/update-score")
    public ResponseEntity<IdResponse> updateScore(
            UpdateScoreRequestDto updateRequestDto) {
        UUID scoreId = teacherAssessmentService.updateScore(updateRequestDto);
        return ResponseEntity
                .ok(new IdResponse(scoreId));
    }

    @Override
    @DeleteMapping("/delete/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        teacherAssessmentService.deleteScore(scoreId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    @PostMapping("/comment")
    public ResponseEntity<IdResponse> addComment(@Valid @RequestBody CreateCommentRequestDto commentRequest) {
        UUID commentId = teacherAssessmentService.addComment(commentRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(commentId));
    }


    @Override
    @GetMapping("/{studentId}/academic-history")
    public void getAcademicHistory(@PathVariable UUID studentId) {

    }
}
