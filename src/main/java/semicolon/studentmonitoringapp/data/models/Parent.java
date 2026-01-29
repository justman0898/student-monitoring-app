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
public class Parent {

    @Id @GeneratedValue @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    private String address;

    @Column(nullable = false)
    @NotNull
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    @Column(nullable = false)
    private Boolean notificationEnabled;


    private String preferredLanguage;

    private Instant createdAt = Instant.now();


    public void addStudent(Student student) {
        students.add(student);
        student.getParents().add(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.getParents().remove(this);
    }
}
