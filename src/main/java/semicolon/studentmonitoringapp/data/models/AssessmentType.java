package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AssessmentType {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String code;

    private String description;

    @Column(nullable = false)
    @NotNull
    private Instant createdAt =  Instant.now();
}
