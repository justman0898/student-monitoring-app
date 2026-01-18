package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateAssessmentTypeRequestDto;
import semicolon.studentmonitoringapp.dtos.request.CreateParentRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.CreateParentResponseDto;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/admin/classes")
public class AdminClassControllerImpl implements AdminClassController {

    @Override
    @PostMapping()
    public void createClass(CreateClassRequestDto classRequestDto) {

    }

    @Override
    @GetMapping()
    public List<ClassResponseDto> getAllClasses() {
        return List.of();
    }

    @Override
    @PutMapping("/{classId}")
    public void updateClass(@PathVariable UUID classId, JsonPatch patch) {

    }

    @Override
    @DeleteMapping("/students/{classId}")
    public void deleteClass(@PathVariable UUID classId) {

    }

    @Override()
    @PostMapping("/{classId}/assign-teacher/{teacherId}")
    public void assignTeacher(@PathVariable UUID classId, @PathVariable UUID teacherId) {

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
}
