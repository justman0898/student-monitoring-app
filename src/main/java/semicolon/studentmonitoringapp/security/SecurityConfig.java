package semicolon.studentmonitoringapp.security;

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
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

@Configuration
public class SecurityConfig {
    @Value("${SECRET_KEY}")
    private String secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean("secretKey")
    public String secretKey() {
        return secretKey;
    }

    @Autowired
    private CustomUserDetails userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Login endpoints - public
                        .requestMatchers(HttpMethod.POST,"/api/v1/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/parent/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/teacher/login").permitAll()
                        
                        // Registration endpoints - public
                        .requestMatchers(HttpMethod.POST, "/api/v1/teachers").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/classes/create-parent").permitAll()
                        
                        // Admin endpoints - require ADMIN role
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/classes").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        
                        // Teacher endpoints - require TEACHER role
                        .requestMatchers("/api/v1/teachers/**").hasRole("TEACHER")
                        
                        // Parent endpoints - require PARENT role
                        .requestMatchers("/api/v1/parent/**").hasRole("PARENT")
                        
                        // Subject endpoints - accessible by ADMIN and TEACHER
                        .requestMatchers(HttpMethod.GET, "/api/v1/subjects").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/api/v1/subjects/**").hasRole("ADMIN")
                        
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .build();
    }


}
