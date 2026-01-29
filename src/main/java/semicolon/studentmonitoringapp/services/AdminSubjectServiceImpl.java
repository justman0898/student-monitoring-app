package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semicolon.studentmonitoringapp.data.models.Subject;
import semicolon.studentmonitoringapp.data.repositories.SubjectRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminSubjectServiceImpl implements AdminSubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolClassMapper schoolClassMapper;


    @Override
    public UUID createSubject(CreateSubjectRequestDto requestDto) {
        Subject subject = schoolClassMapper.toEntity(requestDto);
        Subject saved = subjectRepository.save(subject);
        return saved.getId();
    }

    @Override
    public List<SubjectResponseDto> getSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return mapSubjectsToResponses(subjects);
    }

















    private List<SubjectResponseDto> mapSubjectsToResponses(List<Subject> subjects) {
        return subjects
                .stream().map(schoolClassMapper::toDto)
                .toList();
    }

    @Override
    public void removeSubject(UUID subjectId) {
        subjectRepository.deleteById(subjectId);
    }
}
