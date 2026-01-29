package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Score;

import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
}
