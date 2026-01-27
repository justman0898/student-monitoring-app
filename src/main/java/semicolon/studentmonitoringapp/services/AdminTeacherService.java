package semicolon.studentmonitoringapp.services;

import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminTeacherService {
    TeacherRegistrationDetailsDto registerTeacher(RegisterTeacherRequestDto registerRequestDto);
    List<TeacherResponseDto> getAllActiveTeachers();
    List<TeacherResponseDto> getAllTeachers();
    UUID removeTeacher (UUID teacherId);

    TeacherResponseDto findTeacher(UUID teacherId);
}
