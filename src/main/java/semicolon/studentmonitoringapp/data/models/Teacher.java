package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
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
public class Teacher {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "teacher_id"),
            name = "teacher_class",
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
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

    private String phone;

    @NotNull
    @Column(nullable = false)
    private String generatedPassword;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Gender gender;



    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClasses.add(schoolClass);
        schoolClass.getTeachers().add(this);
    }

    public void removeSchoolClass(SchoolClass schoolClass) {
        schoolClasses.remove(schoolClass);
        schoolClass.getTeachers().remove(this);
    }
}
