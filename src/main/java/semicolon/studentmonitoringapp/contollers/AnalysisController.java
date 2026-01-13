package semicolon.studentmonitoringapp.contollers;

import java.util.UUID;

public interface AnalysisController {

    void getPerformanceHistory(UUID studentId);

    void getWeakSubjects(UUID studentId);



}
