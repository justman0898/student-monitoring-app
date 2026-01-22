package semicolon.studentmonitoringapp.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semicolon.studentmonitoringapp.data.models.SchoolClass;
import semicolon.studentmonitoringapp.data.models.Student;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.StudentResponseDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;

@Mapper(componentModel="spring")
public interface SchoolClassMapper {

    @Mapping(source = "className", target = "name")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "id", ignore = true)
    SchoolClass toEntity(CreateClassRequestDto createClassRequestDto);

    @Mapping(target = "teachers",  ignore = true)
    @Mapping(target = "students",  ignore = true)
    ClassResponseDto toDto(SchoolClass schoolClass);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "schoolClasses", ignore = true)
    TeacherResponseDto toDto(Teacher teacher);

    @Mapping(target = "parents", ignore = true)
    StudentResponseDto toDto(Student student);
}
