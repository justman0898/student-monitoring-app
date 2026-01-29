package semicolon.studentmonitoringapp.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.studentmonitoringapp.data.models.SchoolClass;

import java.util.UUID;


public interface SchoolClassRepository extends JpaRepository<SchoolClass, UUID> {

    Boolean existsByNameIgnoreCaseAndAcademicYear(String name, String academicYear);
}
