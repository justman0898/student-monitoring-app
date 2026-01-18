package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateClassRequestDto {
    @NotNull
    private String className;

    @NotNull
    private String section;

    @NotNull
    private String academicYear;

    @NotNull
    private List<UUID> teacherId;

    @NotEmpty
    private List<UUID> subjectIds;

    @NotEmpty
    private List<UUID> studentIds;




}
