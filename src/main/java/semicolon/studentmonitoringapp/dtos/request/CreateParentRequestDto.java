package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Parent;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateParentRequestDto {
    @NotNull(message = "Cannot be empty")
    @Trimmed
    private String firstName;

    @NotBlank(message = "Cannot be empty")
    @Trimmed
    private String lastName;

    @NotBlank(message = "Cannot be empty")
    @Email(message = "Invalid email address")
    @ExistsInDb(entity = Parent.class,
    field = "email",
            message = "Email already exists"
    )
    @Trimmed
    private String email;

    @NotBlank(message = "Provide an address")
    @Trimmed
    private String address;

    @NotBlank(message = "phon field Cannot be empty")
    @ExistsInDb(entity = Parent.class,
            field = "phone",
            message = "Phone already exists"
    )
    @Pattern(regexp = "^\\d{9,}$", message = "phone number cannot be less than 9")
    @Trimmed
    private String phone;


    private List<UUID> studentIds;

    private Boolean notificationEnabled;


    private String preferredLanguage;
}