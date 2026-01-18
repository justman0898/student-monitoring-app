package semicolon.studentmonitoringapp.contollers;

import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminTeacherController {

   TeacherRegistrationDetailsDto registerTeacher (RegisterTeacherRequestDto registerRequestDto);
   List<TeacherResponseDto> getTeachers ();
   TeacherResponseDto getTeacher (UUID teacherId);
   void removeTeacher (UUID teacherId);

}
