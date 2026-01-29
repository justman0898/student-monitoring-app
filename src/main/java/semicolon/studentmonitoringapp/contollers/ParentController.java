package semicolon.studentmonitoringapp.contollers;

import semicolon.studentmonitoringapp.dtos.response.ScoreResponseDto;
import semicolon.studentmonitoringapp.dtos.response.StudentResponseDto;

import java.util.List;
import java.util.UUID;

public interface ParentController {

   List<StudentResponseDto> getLinkedStudents(UUID parentId);
   List<ScoreResponseDto> viewResults(UUID studentId);



}
