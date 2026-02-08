package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.services.AdminClassService;

import java.net.URI;
import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/classes")
@AllArgsConstructor
public class AdminClassControllerImpl implements AdminClassController {
    private final AdminClassService classService;

    @Override
    @PostMapping("/create")
    public ResponseEntity<IdResponse> createClass(@Valid @RequestBody CreateClassRequestDto classRequestDto) {
        UUID schoolClassId = classService.createClass(classRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(schoolClassId));

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
    @DeleteMapping("de-activate/{classId}")
    public ResponseEntity<?> deleteClass(@PathVariable UUID classId) {
        classService.deactivateClass(classId);
        return ResponseEntity.noContent().build();
    }


    @Override
    @PatchMapping("/{classId}/un-assign/{teacherId}")
    public ResponseEntity<?> unassignTeacher(@PathVariable UUID classId, @PathVariable UUID teacherId) {
        classService.removeTeacherFromClass(classId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/create-parent")
    public ResponseEntity<?> addParent(@Valid @RequestBody CreateParentRequestDto createParentRequestDto) {
        classService.createParentProfile(createParentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(UUID.randomUUID()));
    }

    @Override
    @PatchMapping("/update-parent")
    public ResponseEntity<IdResponse> updateParent(
            @Valid
            @RequestBody
            UpdateParentRequestDto updateParentDto) {
        UUID updated = classService.updateParent(updateParentDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new IdResponse(updated));
    }

    @Override
    @PostMapping("/assessment-type")
    public ResponseEntity<IdResponse> createAssessmentType(@Valid @RequestBody CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto) {
        UUID assessmentTypeId = classService.createAssessmentType(createAssessmentTypeRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(assessmentTypeId));
    }

    @Override
    @PostMapping("/assessment-config")
    public ResponseEntity<IdResponse> createAssessmentConfig(CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto) {
        UUID assessmentConfigId = classService.createAssessmentConfig(createAssessmentConfigRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(assessmentConfigId));
    }

    @Override
    @GetMapping("/{assessmentTypeId}")
    public ResponseEntity<?> getAssessmentType(@PathVariable UUID assessmentTypeId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(classService.getAssessmentType(assessmentTypeId));

    }

    @Override
    @GetMapping("/assessment-types")
    public ResponseEntity<?> getAllAssessmentTypes() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(classService.getAllAssessmentTypes());
    }

    @Override
    @PostMapping("/students")
    public ResponseEntity<IdResponse> registerStudent(
            @Valid
            @RequestBody
            CreateStudentRequestDto createStudentRequestDto) {
        UUID registeredId = classService.registerStudent(createStudentRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new IdResponse(registeredId));
    }


}
