package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;

import java.util.List;
import java.util.UUID;

public interface AdminClassController {

    ResponseEntity<IdResponse> createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> getAllClasses();
    ResponseEntity<?> updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto);
    ResponseEntity<?> deleteClass(UUID classId);
    ResponseEntity<?> unassignTeacher(UUID classId, UUID teacherId);
    ResponseEntity<?> addParent(CreateParentRequestDto createParentRequestDto);
    ResponseEntity<IdResponse> updateParent(UpdateParentRequestDto updateParentDto);
    ResponseEntity<IdResponse> createAssessmentType(CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto);
    ResponseEntity<IdResponse> createAssessmentConfig(CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto);
    ResponseEntity<?> getAssessmentType(UUID assessmentTypeId);
    ResponseEntity<?> getAllAssessmentTypes();
    ResponseEntity<IdResponse> registerStudent(CreateStudentRequestDto createStudentRequestDto);
}
