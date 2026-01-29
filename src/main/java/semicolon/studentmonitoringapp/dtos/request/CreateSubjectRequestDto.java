package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Subject;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;

@Getter
@Setter
public class CreateSubjectRequestDto {
    @NotNull
    @ExistsInDb(
            entity = Subject.class,
            field = "name",
            message = "Subject name already exists"
    )
    private String name;

    @NotNull
    @ExistsInDb(
            entity = Subject.class,
            field = "code",
            message = "Subject code already exists"
    )
    private String code;
}
