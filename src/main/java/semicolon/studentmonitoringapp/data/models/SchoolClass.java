package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class SchoolClass {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private Instant createdAt = Instant.now();

    @ManyToMany(mappedBy = "schoolClasses",  fetch = FetchType.LAZY)
    private Set<Teacher> teachers = new HashSet<>();


}
