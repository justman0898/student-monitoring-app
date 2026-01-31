package semicolon.studentmonitoringapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.data.repositories.*;
import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherAssessmentServiceImplTest {
    @InjectMocks
    TeacherAssessmentServiceImpl teacherAssessmentService;

    @Mock
    ScoreRepository scoreRepository;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    SchoolClassMapper mapper;

    @Mock
    StudentRepository studentRepository;

    @Mock
    AssessmentConfigRepository assessmentConfigRepo;

    @Mock
    CommentRepository commentRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Captor
    ArgumentCaptor<Score> scoreCaptor;

    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    @Mock
    Clock clock;

    @Mock
    SubjectRepository subjectRepository;




    @Test
    void testThatCanSubmitScore(){
        SubmitScoreRequestDto submitScoreRequestDto = new SubmitScoreRequestDto();
        submitScoreRequestDto.setScore(10);

        Score score = new Score();
        score.setId(UUID.randomUUID());

        Student student = new Student();
        student.setId(UUID.randomUUID());
        score.setStudent(student);

        Teacher teacher = new Teacher();
        teacher.setId(UUID.randomUUID());
        score.setTeacher(teacher);


        AssessmentConfig assessmentConfig = new AssessmentConfig();
        assessmentConfig.setId(UUID.randomUUID());
        assessmentConfig.setAcademicYear("2020");
        score.setAssessmentConfig(assessmentConfig);

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        student.setComments(Set.of(comment));

        when(mapper.toEntity(any(SubmitScoreRequestDto.class)))
                .thenReturn(score);
        when(studentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(student));
        when(assessmentConfigRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(assessmentConfig));
        when(teacherRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(teacher));

        UUID submittedScore = teacherAssessmentService.submitScore(submitScoreRequestDto);

        assertThat(submittedScore).isNotNull();
        assertThat(submittedScore).isEqualTo(score.getId());

        verify(scoreRepository).save(scoreCaptor.capture());
        Score savedScore = scoreCaptor.getValue();

        assertThat(savedScore.getId())
                .isEqualTo(score.getId());

        assertThat(savedScore.getStudent()
                .getId())
                .isEqualTo(student.getId());

        assertThat(savedScore.getAssessmentConfig()
                .getAcademicYear())
                .isEqualTo(assessmentConfig.getAcademicYear());

        assertThat(savedScore.getAssessmentConfig()
                .getId())
                .isEqualTo(assessmentConfig.getId());

        assertThat(savedScore.getTeacher()
                .getId())
                .isEqualTo(teacher.getId());
    }


    @Test
    void testThatCanUpdateScore(){

        UpdateScoreRequestDto updateScoreRequestDto = new UpdateScoreRequestDto();
        updateScoreRequestDto.setScore(16);
        updateScoreRequestDto.setId(UUID.randomUUID());

        Score score = new Score();
        score.setId(updateScoreRequestDto.getId());
        score.setScore(10);

        when(scoreRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(score));

        UUID scoreId = teacherAssessmentService.updateScore(updateScoreRequestDto);

        assertThat(scoreId).isNotNull();
        assertThat(scoreId).isEqualTo(updateScoreRequestDto.getId());

        verify(scoreRepository).save(scoreCaptor.capture());
        assertThat(scoreCaptor.getValue()
                .getScore())
                .isEqualTo(updateScoreRequestDto.getScore());

    }

    @Test
    void testThatCanDeleteScore(){
        Instant fixedInstant = Instant.parse("2026-01-29T12:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);


        Score score = new Score();
        score.setId(UUID.randomUUID());
        score.setScore(10);


        when(scoreRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(score));
        when(clock.instant()).thenReturn(fixedInstant);


        teacherAssessmentService.deleteScore(score.getId());
        verify(scoreRepository).save(scoreCaptor.capture());

        assertThat(scoreCaptor.getValue()
                .getIsActive())
                .isEqualTo(false);

        assertThat(scoreCaptor.getValue()
                .getDeletedDate())
                .isEqualTo(fixedInstant);
    }


    @Test
    void testThatCanAddComment() throws Exception {

        CreateCommentRequestDto createCommentRequestDto = new CreateCommentRequestDto();
        createCommentRequestDto.setStudentId(UUID.randomUUID());
        createCommentRequestDto.setTeacherId(UUID.randomUUID());
        createCommentRequestDto.setSubjectId(UUID.randomUUID());

        Comment comment = new Comment();
        comment.setText("This is a comment");
        comment.setId(UUID.randomUUID());

        Teacher teacher = new Teacher();
        teacher.setId(UUID.randomUUID());

        Student student = new Student();
        student.setId(UUID.randomUUID());

        Subject subject = new Subject();
        subject.setId(UUID.randomUUID());

        when(mapper.toEntity(any(CreateCommentRequestDto.class)))
                .thenReturn(comment);
        when(teacherRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(teacher));
        when(studentRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(student));
        when(subjectRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(subject));

        UUID commentId = teacherAssessmentService.addComment(createCommentRequestDto);
        assertThat(commentId).isNotNull();

        verify(commentRepository).save(commentCaptor.capture());
        assertThat(commentCaptor.getValue()
                .getId())
                .isEqualTo(commentId);

        assertThat(commentCaptor.getValue()
                .getTeacher().getId())
                .isEqualTo(teacher.getId());

        assertThat(commentCaptor.getValue().getStudent()
                .getId())
                .isEqualTo(student.getId());

        assertThat(commentCaptor.getValue().getSubject()
                .getId())
                .isEqualTo(subject.getId());
    }
































}