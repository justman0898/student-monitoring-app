package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AssessmentConfig {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AssessmentType  assessmentType;

    @Positive
    @Column(nullable = false)
    @NotNull
    private Integer maxScore;

    @Positive
    @Column(nullable = false)
    @NotNull
    private Integer weight;

    @Column(nullable = false)
    @NotNull
    private String academicYear;

    private Instant createdAt = Instant.now();

}
