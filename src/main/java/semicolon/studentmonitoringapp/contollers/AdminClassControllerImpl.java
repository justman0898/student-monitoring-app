package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateAssessmentTypeRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateParentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateParentResponseDto;
import semicolon.studentmonitoringapp.services.AdminClassService;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/admin/classes")
@AllArgsConstructor
public class AdminClassControllerImpl implements AdminClassController {
    private final AdminClassService classService;

    @Override
    @PostMapping("/create")
    public ResponseEntity<?> createClass(@Valid @RequestBody CreateClassRequestDto classRequestDto) {
        classService.createClass(classRequestDto);
        return ResponseEntity.accepted().build();

    }

    @Override
    @GetMapping()
    public List<ClassResponseDto> getAllClasses() {
        return List.of();
    }

    @Override
    @PatchMapping("/{classId}")
    public ResponseEntity<?> updateClass(@PathVariable UUID classId, @RequestBody SchoolClassPatchRequestDto classPatchDto) {
        classService.updateClass(classId, classPatchDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/students/{classId}")
    public void deleteClass(@PathVariable UUID classId) {

    }


    @Override
    @PostMapping("/{teacherId}")
    public void unassignTeacher(@PathVariable UUID teacherId) {

    }

    @Override
    @PostMapping("/add-parent")
    public CreateParentResponseDto addParent(CreateParentRequestDto createParentRequestDto) {
        return null;
    }

    @Override
    @PostMapping("assessment-types")
    public void createAssessmentType(List<CreateAssessmentTypeRequestDto> createAssessmentTypeRequestDto) {

    }

    @Override
    public void createAssessmentConfig() {

    }
}
