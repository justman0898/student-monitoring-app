package semicolon.studentmonitoringapp.services;

import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminSubjectService {

    UUID createSubject(CreateSubjectRequestDto requestDto);
    List<SubjectResponseDto> getSubjects();
    void removeSubject(UUID subjectId);
}
