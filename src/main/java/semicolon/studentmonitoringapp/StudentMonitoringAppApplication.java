package semicolon.studentmonitoringapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class StudentMonitoringAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentMonitoringAppApplication.class, args);
    }

}
