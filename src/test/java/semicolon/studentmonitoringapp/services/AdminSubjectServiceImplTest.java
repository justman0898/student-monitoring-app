package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import semicolon.studentmonitoringapp.data.models.Subject;
import semicolon.studentmonitoringapp.data.repositories.SubjectRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminSubjectServiceImplTest {

    @InjectMocks
    AdminSubjectServiceImpl subjectService;

    @Mock
    SubjectRepository subjectRepository;

    @Mock
    SchoolClassMapper schoolClassMapper;

    @Mock
    ObjectMapper objectMapper;

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(UUID.randomUUID());
        subject.setName("test");
        subject.setCode("test");

    }


    @Test
    void testThatCanCreateSubject() {

        CreateSubjectRequestDto requestDto = new CreateSubjectRequestDto();
        requestDto.setCode(subject.getCode());
        requestDto.setName(subject.getName());

        when(subjectRepository.save(any(Subject.class)))
                .thenReturn(subject);
        when(schoolClassMapper.toEntity(requestDto)).thenReturn(subject);


        UUID createdSubjectID = subjectService.createSubject(requestDto);
        verify(subjectRepository).save(any(Subject.class));

        assertThat(createdSubjectID).isNotNull();
        assertThat(createdSubjectID).isEqualTo(subject.getId());

    }

    @Test
    void testThatCanGetAllSubjects() {
        SubjectResponseDto responseDto = new SubjectResponseDto();
        responseDto.setCode(subject.getCode());
        responseDto.setName(subject.getName());
        responseDto.setId(subject.getId());

        when(subjectRepository.findAll()).thenReturn(List.of(subject));
        when(schoolClassMapper.toDto(any(Subject.class)))
                .thenReturn(responseDto);

        List<SubjectResponseDto> subjects = subjectService.getSubjects();
        assertThat(subjects).isNotNull();
        assertThat(subjects.size()).isEqualTo(1);
        assertThat(subjects.get(0).getId()).isEqualTo(subject.getId());
    }

}