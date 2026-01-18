package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateParentRequestDto {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String phone;

    @NotNull
    private List<UUID> studentIds;

    @NotNull
    private Boolean notificationEnabled;

    @NotNull
    private String preferredLanguage;
}