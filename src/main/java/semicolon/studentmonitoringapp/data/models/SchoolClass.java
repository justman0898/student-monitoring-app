package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
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

    @Column(nullable = false, updatable = false)
    @NotNull
    private String name;

    @Column(nullable = false, updatable = false)
    @NotNull
    private String academicYear;

    @Column(nullable = false)
    @NotNull
    private Instant createdAt = Instant.now();

    @ManyToMany(mappedBy = "schoolClasses",  fetch = FetchType.LAZY)
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "schoolclass_student",
            joinColumns = @JoinColumn(name = "schoolclass_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    @Column(nullable = false)
    @NotNull
    private Boolean isActive = true;


    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getSchoolClasses().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getSchoolClasses().remove(this);

    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getSchoolClass().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getSchoolClass().remove(this);
    }
}
