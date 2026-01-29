package semicolon.studentmonitoringapp.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.studentmonitoringapp.dtos.response.StudentResponseDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/teachers")
public interface TeacherController {

    @GetMapping("/me")
    TeacherResponseDto getProfile(UUID teacherId);

    @GetMapping("/classes/{classId}/students")
    List<StudentResponseDto> getStudentsInClass(@PathVariable UUID classId);

}
