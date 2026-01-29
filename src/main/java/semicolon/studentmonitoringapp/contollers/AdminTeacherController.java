package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminTeacherController {

   ResponseEntity<TeacherRegistrationDetailsDto> registerTeacher (RegisterTeacherRequestDto registerRequestDto);
   ResponseEntity<List<TeacherResponseDto>> getTeachers ();
   ResponseEntity<TeacherResponseDto> getTeacher (UUID teacherId);
   ResponseEntity<IdResponse> removeTeacher (UUID teacherId);

}
