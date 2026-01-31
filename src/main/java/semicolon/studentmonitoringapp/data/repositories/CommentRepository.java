package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Comment;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
