package semicolon.studentmonitoringapp.utils.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.dtos.request.RegisterEventDto;
import semicolon.studentmonitoringapp.dtos.request.UserRequestDto;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@AllArgsConstructor
public class RegisteredUserListener {
    private final AuthService authService;
    private final SchoolClassMapper schoolClassMapper;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handleTeacherRegistered(RegisterEventDto event) {
        UserRequestDto userRequestDto = schoolClassMapper.toDto(event);
        authService.registerUser(userRequestDto);
        log.info("Teacher registered successfully: {}",
                objectMapper.writeValueAsString(userRequestDto));
    }

}
