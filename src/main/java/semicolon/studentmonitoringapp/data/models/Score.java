package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Score {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false,  fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AssessmentConfig assessmentConfig;

    private Instant recordDate = Instant.now();

    @PositiveOrZero
    @NotNull
    private Integer score;
}
