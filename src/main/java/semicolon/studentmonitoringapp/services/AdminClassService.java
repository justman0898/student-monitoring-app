package semicolon.studentmonitoringapp.services;

import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateAssessmentTypeResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminClassService {

    UUID createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> findAllSchoolClasses();
    void updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto);
    void removeTeacherFromClass(UUID classId, UUID teacher);
    void deactivateClass (UUID classId);
    UUID createParentProfile(CreateParentRequestDto createParentRequestDto);
    UUID createAssessmentType(CreateAssessmentTypeRequestDto assessmentTypeRequestDto);
    UUID createAssessmentConfig(CreateAssessmentConfigRequestDto assessmentConfigRequestDto);
    CreateAssessmentTypeResponseDto getAssessmentType(UUID assessmentTypeId);
    List<CreateAssessmentTypeResponseDto> getAllAssessmentTypes();
}
