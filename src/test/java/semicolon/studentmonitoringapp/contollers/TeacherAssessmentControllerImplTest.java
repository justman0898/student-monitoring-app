package semicolon.studentmonitoringapp.contollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import semicolon.studentmonitoringapp.data.repositories.CommentRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateCommentRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SubmitScoreRequestDto;
import semicolon.studentmonitoringapp.dtos.request.UpdateScoreRequestDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.services.TeacherAssessmentService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeacherAssessmentControllerImpl.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherAssessmentControllerImplTest {

    @MockitoBean
    TeacherAssessmentService teacherService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private Utility utility;



    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetails customUserDetails;

    @MockitoBean
    CommentRepository commentRepository;

    @Test
    void testThatCanSubmitScore()throws Exception {

        SubmitScoreRequestDto submitScoreRequestDto = new SubmitScoreRequestDto();
        submitScoreRequestDto.setScore(10);
        submitScoreRequestDto.setTeacherId(UUID.randomUUID());
        submitScoreRequestDto.setAssessmentConfigId(UUID.randomUUID());
        submitScoreRequestDto.setStudentId(UUID.randomUUID());

        UUID scoreId = UUID.randomUUID();

        when(teacherService.submitScore(any(SubmitScoreRequestDto.class))).thenReturn(scoreId);

        mockMvc.perform(post("/api/v1/teachers/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submitScoreRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value(scoreId.toString()))
                .andReturn();

    }

    @Test
    void testThatCanUpdateScore() throws Exception{
        UpdateScoreRequestDto updateScoreRequestDto = new UpdateScoreRequestDto();
        updateScoreRequestDto.setId(UUID.randomUUID());
        updateScoreRequestDto.setScore(10);

        when(teacherService.updateScore(any()))
                .thenReturn(updateScoreRequestDto.getId());

        mockMvc.perform(patch("/api/v1/teachers/update-score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateScoreRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateScoreRequestDto.getId().toString()))
                .andReturn();
    }

    @Test
    void testThatCanDeleteScore() throws Exception {
        UUID scoreId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/teachers/delete/{scoreId}", scoreId))
                .andExpect(status().isNoContent());

        verify(teacherService, times(1))
                .deleteScore(scoreId);
    }

    @Test
    void testThatCanSubmitComment() throws Exception {

        CreateCommentRequestDto createCommentRequestDto = new CreateCommentRequestDto();
        createCommentRequestDto.setSubjectId(UUID.randomUUID());
        createCommentRequestDto.setTeacherId(UUID.randomUUID());
        createCommentRequestDto.setSubjectId(UUID.randomUUID());
        createCommentRequestDto.setText("Text");

        UUID  commentId = UUID.randomUUID();

        when(teacherService.addComment(any(CreateCommentRequestDto.class)))
                .thenReturn(commentId);

        mockMvc.perform(post("/api/v1/teachers/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value(commentId.toString()));

    }












}