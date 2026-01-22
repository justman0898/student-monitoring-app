package semicolon.studentmonitoringapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import semicolon.studentmonitoringapp.data.models.Teacher;

import java.util.*;

@Getter
@Setter
public class ClassResponseDto {

    private UUID id;
    private String name;
    private String academicYear;
    private List<TeacherResponseDto> teachers = new ArrayList<>();
    private List<StudentResponseDto> students = new ArrayList<>();


}