package semicolon.studentmonitoringapp.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.*;

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


    @Mapping(target = "schoolClasses", ignore = true)
    TeacherResponseDto toDto(Teacher teacher);

    @Mapping(target = "parents", ignore = true)
    StudentResponseDto toDto(Student student);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Parent toEntity(CreateParentRequestDto createParentRequestDto);

    @Mapping(target = "code", ignore = true)
    AssessmentType toEntity(CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto);
    CreateAssessmentTypeResponseDto toDto(AssessmentType assessmentType);

    @Mapping(target = "createdAt", ignore = true)
    AssessmentConfig toEntity(CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto);

    Teacher toEntity(RegisterTeacherRequestDto registerRequestDto);

    Subject toEntity(CreateSubjectRequestDto requestDto);

    SubjectResponseDto toDto(Subject subject);

    Score toEntity(SubmitScoreRequestDto scoreRequestDto);

    Comment toEntity(CreateCommentRequestDto commentRequestDto);

    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterUserRequestDto registerUserRequestDto);
}
