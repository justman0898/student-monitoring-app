package semicolon.studentmonitoringapp.data.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Student student;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private Instant createdAt;
}
