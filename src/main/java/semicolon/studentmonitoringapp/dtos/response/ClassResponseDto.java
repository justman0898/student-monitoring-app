package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Teacher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ClassResponseDto {

    private UUID id;
    private String name;
    private Set<TeacherResponseDto> teachers = new HashSet<>();
}
