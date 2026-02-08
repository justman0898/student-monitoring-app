package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;
import semicolon.studentmonitoringapp.utils.annotations.ValidAcademicYear;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class CreateClassRequestDto {
    @NotBlank(message = "Enter a class name")
    @Trimmed
    private String className;

    @NotBlank(message = "enter an academic year")
    @Trimmed
    @ValidAcademicYear
    private String academicYear;


    @NotNull
    private List<UUID> teacherIds = new ArrayList<>();

    @NotNull
    private List<UUID> studentIds = new ArrayList<>();




}
