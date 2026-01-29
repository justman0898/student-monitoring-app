package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;
import semicolon.studentmonitoringapp.services.AdminSubjectService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/subjects")
@AllArgsConstructor
public class AdminSubjectControllerImpl implements AdminSubjectController {
    private final AdminSubjectService adminSubjectService;



    @Override
    @PostMapping
    public ResponseEntity<?> createSubject(@Valid @RequestBody CreateSubjectRequestDto subjectRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(adminSubjectService.createSubject(subjectRequestDto)));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> getSubjects() {
        return ResponseEntity
                .ok(adminSubjectService.getSubjects());
    }

    @Override
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable UUID subjectId) {
        adminSubjectService.removeSubject(subjectId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
