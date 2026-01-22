package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Parent;

import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
}
