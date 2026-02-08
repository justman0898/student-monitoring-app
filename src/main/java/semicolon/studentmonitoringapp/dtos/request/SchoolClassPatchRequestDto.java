package semicolon.studentmonitoringapp.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SchoolClassPatchRequestDto {
    private Set<UUID> teachers = new HashSet<>();
    private Set<UUID> students = new HashSet<>();
}
