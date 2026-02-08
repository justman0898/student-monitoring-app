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
import semicolon.studentmonitoringapp.dtos.request.*;
import semicolon.studentmonitoringapp.dtos.response.CreateAssessmentTypeResponseDto;
import semicolon.studentmonitoringapp.security.CustomUserDetails;
import semicolon.studentmonitoringapp.security.JwtProvider;
import semicolon.studentmonitoringapp.services.AdminClassService;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminClassControllerImpl.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminClassControllerImplTest {

    @MockitoBean
    private AdminClassService classService;

    @MockitoBean
    private EntityManager entityManager;

    @MockitoBean
    private TypedQuery<Long> query;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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



    @Test
    void testThatCanCreateClass() throws Exception {
        CreateClassRequestDto createClassRequestDto = new CreateClassRequestDto();
        createClassRequestDto.setClassName("test");
        createClassRequestDto.setStudentIds(List.of(UUID.randomUUID()));
        createClassRequestDto.setTeacherIds(List.of(UUID.randomUUID()));
        createClassRequestDto.setAcademicYear("2025/2026");

        mockMvc.perform(post("/api/v1/admin/classes/create")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createClassRequestDto)))
                .andExpect(status().isCreated());

        verify(classService, times(1)).createClass(any(CreateClassRequestDto.class));

    }

    @Test
    void testThatCanFindAllClasses() {
//        when(classService.findAllSchoolClasses()).thenReturn();
    }

    @Test
    void testThatCanUpdateSchoolClass() throws Exception {
        UUID teacherId_1 = UUID.randomUUID();
        UUID teacherId_2 = UUID.randomUUID();

        SchoolClassPatchRequestDto patchRequestDto = new SchoolClassPatchRequestDto();
        patchRequestDto.setTeachers(Set.of(teacherId_1, teacherId_2));

        mockMvc.perform(patch("/api/v1/admin/classes/{classId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequestDto)))
                .andExpect(status().isNoContent());

        verify(classService, times(1)).updateClass(any(),any());
    }

    @Test
    void testThatCanUnassignTeacherFromClass() throws Exception {

        mockMvc.perform(patch("/api/v1/admin/classes/{classId}/un-assign/{teacherId}", UUID.randomUUID(),UUID.randomUUID()))
                .andExpect(status().isNoContent());

        verify(classService, times((1))).removeTeacherFromClass(any(),any());

    }

    @Test
    void testThatCanDeActivateClass() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/classes/de-activate/{classId}", UUID.randomUUID() ))
                .andExpect(status().is2xxSuccessful());

        verify(classService).deactivateClass(any());

    }

    @Test
    void testThatCanCreateParentProfile() throws Exception {
        CreateParentRequestDto createParentRequestDto = new CreateParentRequestDto();
        createParentRequestDto.setFirstName("test");
        createParentRequestDto.setAddress("test");
        createParentRequestDto.setLastName("test");
        createParentRequestDto.setNotificationEnabled(false);
        createParentRequestDto.setPreferredLanguage("English");
        createParentRequestDto.setEmail("jay@gmail.com");
        createParentRequestDto.setPhone("09068325094");

        when(entityManager.createQuery(anyString(),eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(),any(String.class)))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        mockMvc.perform(post("/api/v1/admin/classes/create-parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createParentRequestDto)))
                .andExpect(status().isCreated());

        verify(classService).createParentProfile(any());

    }

    @Test
    void testThatCanCreateAnAssessmentType() throws Exception {
        CreateAssessmentTypeRequestDto createAssessmentTypeRequestDto = new CreateAssessmentTypeRequestDto();
        createAssessmentTypeRequestDto.setDescription("test");
        createAssessmentTypeRequestDto.setCode("test");

        when(entityManager.createQuery(anyString(),eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(),any(String.class)))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        mockMvc.perform(post("/api/v1/admin/classes/assessment-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAssessmentTypeRequestDto)))
                .andExpect(status().isCreated());

        verify(classService).createAssessmentType(any());
    }

    @Test
    void testThatCanCreateValidAssessmentConfig() throws Exception {

        CreateAssessmentConfigRequestDto createAssessmentConfigRequestDto = new CreateAssessmentConfigRequestDto();
        createAssessmentConfigRequestDto.setSchoolClassId(UUID.randomUUID());
        createAssessmentConfigRequestDto.setSubjectId(UUID.randomUUID());
        createAssessmentConfigRequestDto.setWeight(1);
        createAssessmentConfigRequestDto.setAssessmentTypeId(UUID.randomUUID());
        createAssessmentConfigRequestDto.setMaxScore(100);
        createAssessmentConfigRequestDto.setAcademicYear("2015/2016");

        mockMvc.perform(post("/api/v1/admin/classes/assessment-config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAssessmentConfigRequestDto)))
                .andExpect(status().isCreated());

        verify(classService).createAssessmentConfig(any());

    }

    @Test
    void testThatCanGetAssessmentType() throws Exception {
        CreateAssessmentTypeResponseDto createAssessmentTypeResponseDto = new CreateAssessmentTypeResponseDto();
        createAssessmentTypeResponseDto.setId(UUID.randomUUID());
        when(classService.getAssessmentType(any()))
                .thenReturn(createAssessmentTypeResponseDto);

        mockMvc.perform(get("/api/v1/admin/classes/{assessmentTypeId}", UUID.randomUUID()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createAssessmentTypeResponseDto.getId().toString()));
    }

    @Test
    void testThatCanGetAllAssessmentTypes() throws Exception {

        CreateAssessmentTypeResponseDto assessmentTypeResponseDto = new  CreateAssessmentTypeResponseDto();
        assessmentTypeResponseDto.setId(UUID.randomUUID());

        CreateAssessmentTypeResponseDto assessmentTypeResponseDto2 = new CreateAssessmentTypeResponseDto();
        assessmentTypeResponseDto2.setId(UUID.randomUUID());

        when(classService.getAllAssessmentTypes())
                .thenReturn(List.of(assessmentTypeResponseDto, assessmentTypeResponseDto2));

        mockMvc.perform(get("/api/v1/admin/classes/assessment-types"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id")
                        .value(assessmentTypeResponseDto
                                .getId().toString()));


    }

    @Test
    void testThatCanUpdateParent() throws Exception {
        UpdateParentRequestDto updateParentRequestDto = new UpdateParentRequestDto();
        updateParentRequestDto.setId(UUID.randomUUID());

        when(classService.updateParent(any()))
                .thenReturn(updateParentRequestDto.getId());

        mockMvc.perform(patch("/api/v1/admin/classes/update-parent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateParentRequestDto)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(updateParentRequestDto.getId().toString()));

    }

    @Test
    void testThatCanCreateStudent()throws Exception {
        CreateStudentRequestDto createStudentRequestDto = new CreateStudentRequestDto();
        createStudentRequestDto.setFirstName("Justice");
        createStudentRequestDto.setLastName("Igboneme");
        createStudentRequestDto.setEmail("test@test.com");
        createStudentRequestDto.setGender(Gender.MALE);

        UUID saved = UUID.randomUUID();

        when(entityManager.createQuery(anyString(),eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(),any(String.class)))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        when(classService.registerStudent(any()))
                .thenReturn(saved);

        mockMvc.perform(post("/api/v1/admin/classes/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStudentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(saved.toString()));
    }
















}