package semicolon.studentmonitoringapp.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.studentmonitoringapp.dtos.response.ScoreResponseDto;
import semicolon.studentmonitoringapp.dtos.response.StudentResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/parent")
public class ParentControllerImpl implements ParentController {
    @Override
    @GetMapping("/students/{parentId}")
    public List<StudentResponseDto> getLinkedStudents(@PathVariable UUID parentId) {
        return List.of();
    }

    @Override
    @GetMapping("{studentId}")
    public List<ScoreResponseDto> viewResults(@PathVariable UUID studentId) {
        return List.of();
    }
}
