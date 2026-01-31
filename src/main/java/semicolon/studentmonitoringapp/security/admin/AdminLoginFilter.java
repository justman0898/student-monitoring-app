package semicolon.studentmonitoringapp.security.admin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@AllArgsConstructor
@Slf4j
public class AdminLoginFilter extends OncePerRequestFilter {
    private final Utility utility;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();
        String requestURI = "/api/v1/admin/login";

        if (requestURI.equals(servletPath) && HttpMethod.POST.matches(request.getMethod())) {
            LoginRequestDto loginRequestDto = utility.extractRequest(request);
            log.info(loginRequestDto.toString());
            Boolean authenticated = authService.authenticate(loginRequestDto, response);

            
            log.info("Authenticated: {}", authenticated.toString());

            if (!authenticated) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Invalid credentials\"}");
                response.getWriter().flush();
                return;
            }
            return;

        }
        filterChain.doFilter(request, response);
    }


}
