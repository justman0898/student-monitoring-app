package semicolon.studentmonitoringapp.security.parent;

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
import semicolon.studentmonitoringapp.security.teacher.TeacherLoginFilter;
import semicolon.studentmonitoringapp.services.AuthService;
import semicolon.studentmonitoringapp.utils.Utility;

import java.io.IOException;
@AllArgsConstructor
@Component
@Slf4j
public class ParentLoginFilter extends OncePerRequestFilter {
    private final Utility  utility;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/api/v1/parent/login".equals(request.getServletPath()) &&
                HttpMethod.POST.matches(request.getMethod())){

            if (TeacherLoginFilter.performFilter(request, response, utility, log, authService)) return;
        }
        filterChain.doFilter(request, response);
    }
}
