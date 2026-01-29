package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;

import java.util.UUID;

public interface TeacherAssessmentController {


    void submitScore(SubmitScoreRequestDto submitScoreRequestDto);
    void updateScore(UUID subjectId, JsonPatch jsonPatch);
    void deleteScore(UUID subjectId,  JsonPatch jsonPatch);
    void addSubjectComment(UUID subjectId, JsonPatch jsonPatch);
    void addStudentComment(UUID studentId, JsonPatch jsonPatch);
    void getAcademicHistory(UUID studentId);

}
