package semicolon.studentmonitoringapp.contollers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import semicolon.studentmonitoringapp.data.models.Gender;
import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.RegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.services.AdminTeacherService;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminTeacherController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminTeacherControllerImplTest {

    @MockitoBean
    private AdminTeacherService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private Utility utility;

    @MockitoBean
    private EntityManager entityManager;

    @MockitoBean
    private TypedQuery<Long> query;

    @MockitoBean
    private UserDetailsService userDetailsService;


    @MockitoBean
    private CustomUserDetails customUserDetails;


    @Test
    void testThatCanRegisterTeacher() throws Exception {
        RegisterTeacherRequestDto registerTeacherRequestDto = new RegisterTeacherRequestDto();
        registerTeacherRequestDto.setFirstName("firstName");
        registerTeacherRequestDto.setLastName("lastName");
        registerTeacherRequestDto.setEmail("email@email");
        registerTeacherRequestDto.setGender(Gender.FEMALE);
        registerTeacherRequestDto.setPhone("09068325094");


        when(service.registerTeacher(any()))
                .thenReturn(new RegistrationDetailsDto());
        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(), any(String.class)))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        mockMvc.perform(post("/api/v1/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerTeacherRequestDto)))
                .andExpect(status().isCreated());

        verify(service).registerTeacher(any());
    }

    @Test
    void testThatCanGetAllTeachers() throws Exception {

        when(service.getAllActiveTeachers())
                .thenReturn(List.of(new TeacherResponseDto()));

        mockMvc.perform(get("/api/v1/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testThatCanRemoveTeacher() throws Exception {
        UUID removedTeacherId = UUID.randomUUID();

        when(service.removeTeacher(any()))
                .thenReturn(removedTeacherId);

        mockMvc.perform(delete("/api/v1/teachers/{teacherId}", removedTeacherId))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(removedTeacherId.toString()));
    }

    @Test
    void testThatCanGetTeacher() throws Exception {
        TeacherResponseDto teacherResponseDto = new TeacherResponseDto();
        teacherResponseDto.setId(UUID.randomUUID());

        when(service.findTeacher(any(UUID.class)))
                .thenReturn(teacherResponseDto);

        mockMvc.perform(get("/api/v1/teachers/{teacherId}",
                teacherResponseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(teacherResponseDto.getId().toString()));
    }

}