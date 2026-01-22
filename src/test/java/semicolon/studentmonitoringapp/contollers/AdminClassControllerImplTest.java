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
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminClassControllerImpl.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminClassControllerImplTest {

    @MockitoBean
    private AdminClassService classService;

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
                .andExpect(status().isAccepted());

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

}