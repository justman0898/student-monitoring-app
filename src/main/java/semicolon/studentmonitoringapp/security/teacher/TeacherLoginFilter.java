package semicolon.studentmonitoringapp.security.teacher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import semicolon.studentmonitoringapp.dtos.request.LoginRequestDto;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;

import java.io.IOException;
@AllArgsConstructor
@Slf4j
@Component
public class TeacherLoginFilter extends OncePerRequestFilter {
    private final Utility utility;
    private final AuthService authService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("/api/v1/teacher/login".equals(request.getServletPath()) &&
                HttpMethod.POST.matches(request.getMethod())){

            if (performFilter(request, response, utility, log, authService)) return;
            return;
        }
        filterChain.doFilter(request, response);
    }





















    public static boolean performFilter(HttpServletRequest request, HttpServletResponse response, Utility utility, Logger log, AuthService authService) throws IOException {
        LoginRequestDto loginRequestDto = utility.extractRequest(request);
        log.info("Request: {}", loginRequestDto);
        Boolean authenticated = authService.authenticate(loginRequestDto, response);
        log.info("Authenticated: {}", authenticated);

        if (!authenticated) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid credentials\"}");
            response.getWriter().flush();
            return true;
        }
        return false;
    }

}
