package semicolon.studentmonitoringapp.contollers;

import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.RegisterRequestDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
public class AdminTeacherControllerImpl implements AdminTeacherController {

    @Override
    @PostMapping
    public TeacherRegistrationDetailsDto registerTeacher(RegisterRequestDto registerRequestDto) {
        return null;
    }

    @Override
    @GetMapping
    public List<TeacherResponseDto> getTeachers() {
        return List.of();
    }

    @Override
    @GetMapping("/{teacherId}")
    public TeacherResponseDto getTeacher(@PathVariable UUID teacherId) {
        return null;
    }

    @Override
    @DeleteMapping("/{teacherId}")
    public void removeTeacher(@PathVariable UUID teacherId) {

    }
}
