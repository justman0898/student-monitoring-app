package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Subject;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
