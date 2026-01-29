package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {


    List<Teacher> findAllByIsActiveTrue();


}
