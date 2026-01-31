package semicolon.studentmonitoringapp.dtos.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import semicolon.studentmonitoringapp.security.config.StringTrimDeserializer;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

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
    private String academicYear;


    private List<UUID> teacherIds;


    private List<UUID> studentIds;




}
