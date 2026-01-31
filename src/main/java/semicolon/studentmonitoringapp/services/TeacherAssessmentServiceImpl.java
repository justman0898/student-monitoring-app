package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.*;
import semicolon.studentmonitoringapp.data.repositories.*;
import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TeacherAssessmentServiceImpl implements TeacherAssessmentService {
    private final SchoolClassMapper schoolClassMapper;
    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final AssessmentConfigRepository assessmentConfigRepository;
    private final TeacherRepository teacherRepository;
    private final CommentRepository commentRepository;
    private final Clock clock;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UUID submitScore(SubmitScoreRequestDto scoreRequestDto) {
        Score score = schoolClassMapper.toEntity(scoreRequestDto);
        score.setStudent(getStudent(score.getId()));
        score.setAssessmentConfig(getAssessmentConfig(score.getAssessmentConfig().getId()));
        score.setTeacher(getTeacher(score.getTeacher().getId()));
        scoreRepository.save(score);
        log.info("Score submitted: {}",
                objectMapper.writeValueAsString(score));
        return score.getId();
    }

    @Override
    @Transactional
    public UUID updateScore(UpdateScoreRequestDto scoreRequestDto) {
        Score foundScore = getScore(scoreRequestDto.getId());
        foundScore.setScore(scoreRequestDto.getScore());
        scoreRepository.save(foundScore);
        log.info("Updated Score: {}",
                objectMapper.writeValueAsString(foundScore));
        return foundScore.getId();
    }

    @Override
    @Transactional
    public void deleteScore(UUID scoreId) {
        Score found = getScore(scoreId);
        found.setIsActive(false);
        found.setDeletedDate(Instant.now(clock));
        scoreRepository.save(found);
        log.info("Deleted Score: {}",
                objectMapper.writeValueAsString(found));
    }

    @Override
    public UUID addComment(CreateCommentRequestDto commentRequestDto) {
        Comment comment = schoolClassMapper.toEntity(commentRequestDto);
        Teacher teacher = getTeacher(commentRequestDto.getTeacherId());
        comment.setTeacher(teacher);
        Student student = getStudent(commentRequestDto.getStudentId());
        comment.setStudent(student);
        Subject subject = getSubject_not_found(commentRequestDto);
        comment.setSubject(subject);
        commentRepository.save(comment);
        log.info("Added new Comment: {}",
                objectMapper.writeValueAsString(comment));
        return comment.getId();
    }



    private Subject getSubject_not_found(CreateCommentRequestDto commentRequestDto) {
        return subjectRepository
                .findById(commentRequestDto.getSubjectId())
                .orElseThrow(() -> {
                    NotFoundException exception = new NotFoundException("Subject not found");
                    log.error(exception.getMessage());
                    return exception;
                });
    }


    private Teacher getTeacher(UUID scoreId) {
        return teacherRepository
                .findById(scoreId)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));
    }

    private AssessmentConfig getAssessmentConfig(UUID scoreId) {
        return assessmentConfigRepository
                .findById(scoreId)
                .orElseThrow(() -> new NotFoundException("Assessment Configuration not found"));
    }

    private Student getStudent(UUID scoreId) {
        return studentRepository
                .findById(scoreId)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    private Score getScore(UUID scoreId) {
        return scoreRepository.findById(scoreId)
                .orElseThrow(() -> new NotFoundException("Score not found"));
    }
}
