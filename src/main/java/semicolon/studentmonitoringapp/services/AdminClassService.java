package semicolon.studentmonitoringapp.services;

import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminClassService {

    void createClass(CreateClassRequestDto classRequestDto);
    List<ClassResponseDto> findAllSchoolClasses();
    void updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto);
}
