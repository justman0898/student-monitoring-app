package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateAssessmentTypeRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateParentRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateParentResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminClassController {

    void createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> getAllClasses();
    void updateClass(UUID classId, JsonPatch patch);
    void deleteClass(UUID classId);
    void assignTeacher(UUID classId, UUID teacherId);
    void unassignTeacher(UUID teacherId);
    CreateParentResponseDto addParent(CreateParentRequestDto createParentRequestDto);
    void createAssessmentType(List<CreateAssessmentTypeRequestDto> createAssessmentTypeRequestDto);
    void createAssessmentConfig();

}
