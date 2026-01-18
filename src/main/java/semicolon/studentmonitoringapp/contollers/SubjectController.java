package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface SubjectController {

    void createSubject(CreateSubjectRequestDto subjectRequestDto);
    List<SubjectResponseDto> getSubjects();
    void updateSubject(UUID subjectId, JsonPatch jsonPatch);
    void deleteSubject(UUID subjectId);
}
