package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Student;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
