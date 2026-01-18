package semicolon.studentmonitoringapp.contollers;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/subjects")
public class SubjectControllerImpl implements SubjectController {
    @Override
    @PostMapping
    public void createSubject(CreateSubjectRequestDto subjectRequestDto) {

    }

    @Override
    @GetMapping
    public List<SubjectResponseDto> getSubjects() {
        return List.of();
    }

    @Override
    @PatchMapping("/{subjectId}")
    public void updateSubject(@PathVariable UUID subjectId, JsonPatch jsonPatch) {

    }

    @Override
    @DeleteMapping("/{subjectId}")
    public void deleteSubject(@PathVariable UUID subjectId) {

    }
}
