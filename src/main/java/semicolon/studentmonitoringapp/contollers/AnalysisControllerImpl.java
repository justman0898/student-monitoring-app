package semicolon.studentmonitoringapp.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysis")
public class AnalysisControllerImpl implements AnalysisController {
    @Override
    public void getPerformanceHistory(UUID studentId) {

    }

    @Override
    @GetMapping("/weak-subjects")
    public void getWeakSubjects(UUID studentId) {

    }
}
