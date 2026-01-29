package semicolon.studentmonitoringapp.contollers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;
import semicolon.studentmonitoringapp.services.AdminTeacherService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
public class AdminTeacherControllerImpl implements AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    public AdminTeacherControllerImpl(AdminTeacherService adminTeacherService) {
        this.adminTeacherService = adminTeacherService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TeacherRegistrationDetailsDto> registerTeacher(@Valid @RequestBody RegisterTeacherRequestDto registerRequestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(adminTeacherService.registerTeacher(registerRequestDto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> getTeachers() {
        return ResponseEntity
                .ok(adminTeacherService.getAllActiveTeachers());
    }

    @Override
    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponseDto> getTeacher(@PathVariable UUID teacherId) {
        return ResponseEntity
                .ok(adminTeacherService.findTeacher(teacherId));
    }

    @Override
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<IdResponse> removeTeacher(@PathVariable UUID teacherId) {

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new IdResponse(adminTeacherService.removeTeacher(teacherId)));
    }
}
