package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @ManyToMany(mappedBy = "students")
    private Set<SchoolClass> schoolClass;

    @OneToMany
    private Set<Comment> comments;

    @Email
    private String email;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Parent> parents = new HashSet<>();

    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;




}
