package semicolon.studentmonitoringapp.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.AssessmentConfig;
import java.util.UUID;

public interface AssessmentConfigRepository extends JpaRepository <AssessmentConfig, UUID> {
}
