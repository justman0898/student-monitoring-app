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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import semicolon.studentmonitoringapp.dtos.request.RegisterUserRequestDto;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration =  SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;


    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private Utility utility;

    @MockitoBean
    private EntityManager entityManager;

    @MockitoBean
    private TypedQuery<Long> query;

    @Test
    void testThatCanRegisterUser() throws Exception {

        RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto();
        registerUserRequestDto.setEmail("jay@gmail.com");
        registerUserRequestDto.setPassword("Password1&");

        UUID userId = UUID.randomUUID();

        when(authService.registerAdmin(any())).thenReturn(userId);
        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(query);
        when(query.setParameter(anyString(), any()))
                .thenReturn(query);
        when(query.getSingleResult())
                .thenReturn(0L);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()));



















    }

}