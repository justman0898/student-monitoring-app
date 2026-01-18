package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;

public class CreateSubjectRequestDto {
    @NotNull
    private String subjectName;

    @NotNull
    private String subjectCode;
}
