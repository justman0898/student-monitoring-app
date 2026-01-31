package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Teacher {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToMany(mappedBy = "teachers",fetch = FetchType.LAZY)
    private Set<SchoolClass> schoolClasses =  new HashSet<>();

    private Instant createdAt = Instant.now();

    @NotNull
    @Column(nullable = false)
    private  String firstName;

    @NotNull
    @Column(nullable = false)
    private  String lastName;

    @NotNull
    @Column(nullable = false)
    private  String email;

    @NotNull
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private String generatedPassword;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Gender gender;

    private String address;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive = true;
    private Instant deletedAt;




}
