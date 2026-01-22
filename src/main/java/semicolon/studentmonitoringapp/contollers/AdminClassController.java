package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateAssessmentTypeRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateParentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateParentResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminClassController {

    ResponseEntity<?> createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> getAllClasses();
    ResponseEntity<?> updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto);
    void deleteClass(UUID classId);
    void unassignTeacher(UUID teacherId);
    CreateParentResponseDto addParent(CreateParentRequestDto createParentRequestDto);
    void createAssessmentType(List<CreateAssessmentTypeRequestDto> createAssessmentTypeRequestDto);
    void createAssessmentConfig();

}
