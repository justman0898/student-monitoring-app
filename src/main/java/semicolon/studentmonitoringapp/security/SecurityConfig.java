package semicolon.studentmonitoringapp.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import semicolon.studentmonitoringapp.security.admin.AdminLoginFilter;
import semicolon.studentmonitoringapp.security.config.StringTrimDeserializer;
import semicolon.studentmonitoringapp.security.parent.ParentLoginFilter;
import semicolon.studentmonitoringapp.security.teacher.TeacherLoginFilter;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

import java.io.IOException;
import java.time.Clock;

@Configuration
public class SecurityConfig {
    @Value("${SECRET_KEY}")
    private String secretKey;
    @Autowired
    private StringTrimDeserializer stringTrimDeserializer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Module stringTrimModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, stringTrimDeserializer);
        return module;
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }


    @Bean("secretKey")
    public String secretKey() {
        return secretKey;
    }

    @Autowired
    private CustomUserDetails userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationFilter jwtAuthorizationFilter, AdminLoginFilter adminLoginFilter, ParentLoginFilter parentLoginFilter, TeacherLoginFilter teacherLoginFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/v1/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/parent/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/teacher/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/subjects/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/student/classes").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/teachers/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/teachers/").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/teachers/{teacherId}")
                                        .hasAnyRole("STUDENT", "ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/teachers/{teacherId}").hasRole("ADMIN")
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(adminLoginFilter, JwtAuthorizationFilter.class)
                .addFilterBefore(parentLoginFilter, JwtAuthorizationFilter.class)
                .addFilterBefore(teacherLoginFilter, JwtAuthorizationFilter.class)
                .build();
    }


}
