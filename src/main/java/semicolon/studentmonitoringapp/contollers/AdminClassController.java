package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateParentResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminClassController {

    ResponseEntity<?> createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> getAllClasses();
    ResponseEntity<?> updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto);
    ResponseEntity<?> deleteClass(UUID classId);
    ResponseEntity<?> unassignTeacher(UUID classId, UUID teacherId);
    ResponseEntity<CreateParentResponseDto> addParent(CreateParentRequestDto createParentRequestDto);
    ResponseEntity<?> createAssessmentType(CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto);
    ResponseEntity<?> createAssessmentConfig(CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto);
    ResponseEntity<?> getAssessmentType(UUID assessmentTypeId);
    ResponseEntity<?> getAllAssessmentTypes();
}
