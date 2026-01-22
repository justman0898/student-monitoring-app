package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/teachers")
public class TeacherAssessmentControllerImpl implements TeacherAssessmentController {

    @Override
    @PostMapping("/submit")
    public void submitScore(SubmitScoreRequestDto submitScoreRequestDto) {

    }

    @Override
    @PatchMapping("/{subjectId}")
    public void updateScore(@PathVariable UUID subjectId, JsonPatch jsonPatch) {

    }

    @Override
    @DeleteMapping("/{subjectId}")
    public void deleteScore(@PathVariable UUID subjectId, JsonPatch jsonPatch) {

    }

    @Override
    @PostMapping("/{subjectId}/subject-comment")
    public void addSubjectComment(@PathVariable UUID subjectId, JsonPatch jsonPatch) {

    }

    @Override
    @PostMapping("/{studentId}/comment")
    public void addStudentComment(@PathVariable UUID studentId, JsonPatch jsonPatch) {

    }

    @Override
    @GetMapping("/{studentId}/academic-history")
    public void getAcademicHistory(@PathVariable UUID studentId) {

    }
}
