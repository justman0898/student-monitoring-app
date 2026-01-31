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
    @NotNull(message = "Enter a class name")
    private String className;

    @NotNull(message = "enter an academic year")
    private String academicYear;


    private List<UUID> teacherIds;


    private List<UUID> studentIds;




}
