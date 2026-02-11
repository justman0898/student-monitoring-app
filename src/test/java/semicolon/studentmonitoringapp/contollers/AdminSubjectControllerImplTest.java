package semicolon.studentmonitoringapp.contollers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import semicolon.studentmonitoringapp.dtos.request.CreateSubjectRequestDto;
import semicolon.studentmonitoringapp.dtos.response.IdResponse;
import semicolon.studentmonitoringapp.dtos.response.SubjectResponseDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.services.AdminSubjectService;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AdminSubjectController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminSubjectControllerImplTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private EntityManager entityManager;

    @MockitoBean
    private TypedQuery<Long> query;

    @MockitoBean
    private Utility utility;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetails customUserDetails;


    @MockitoBean
    AdminSubjectService subjectService;

    private CreateSubjectRequestDto requestDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        requestDto = new CreateSubjectRequestDto();
        requestDto.setCode("code");
        requestDto.setName("name");
    }

    @Test
    void testThatCanCreateSubject() throws Exception {
        String createdSubjectId = UUID.randomUUID().toString();

        when(subjectService.createSubject(any()))
                .thenReturn(UUID.fromString(createdSubjectId));
        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(), any()))
                .thenReturn(query);
        when(query.getSingleResult())
                .thenReturn(0L);

        mockMvc.perform(post("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value(createdSubjectId))
                .andExpect(content().string(objectMapper
                        .writeValueAsString(new IdResponse(UUID.fromString(createdSubjectId)))))
                .andReturn();
    }

    @Test
    void testThatCanGetSubjects() throws Exception {
        SubjectResponseDto subjectResponseDto = new SubjectResponseDto();
        subjectResponseDto.setId(UUID.randomUUID());

        when(subjectService.getSubjects())
                .thenReturn(List.of(subjectResponseDto));


        mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(subjectResponseDto.getId().toString()));
    }

    @Test
    void testThatCanDeleteSubject() throws Exception {

        mockMvc.perform(delete("/api/v1/subjects/{subjectId}", UUID.randomUUID()))
                .andExpect(status().isNoContent());

        verify(subjectService).removeSubject(any());
    }
}