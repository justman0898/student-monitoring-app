package semicolon.studentmonitoringapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateParentRequestDto {
    @NotNull
    private UUID id;
    private String address;
    private List<UUID> studentIds = new ArrayList<>();

    @Pattern(regexp = "^\\d{9,}$", message = "phone number cannot be less than 9")
    private String phone;
}
