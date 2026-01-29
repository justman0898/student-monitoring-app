package semicolon.studentmonitoringapp.contollers;

import org.springframework.http.ResponseEntity;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminSubjectController {

    ResponseEntity<?> createSubject(CreateSubjectRequestDto subjectRequestDto);
    ResponseEntity<List<SubjectResponseDto>> getSubjects();
    ResponseEntity<?> deleteSubject(UUID subjectId);
}
