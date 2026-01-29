package semicolon.studentmonitoringapp.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SchoolClassPatchRequestDto {
    private Set<UUID> teachers;
    private Set<UUID> students;
}
