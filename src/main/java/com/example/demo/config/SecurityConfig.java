package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, "/login")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/register")
                    .permitAll()
                    .requestMatchers("/web/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/promotions")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .requestMatchers(HttpMethod.GET, "/promotions/{promotionId}/graduates")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/promotions/{promotionId}/graduates/export")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/promotions/{promotionId}/graduates")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/promotions/{promotionId}/graduates/export")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/groups")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/teachers")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/academic-years")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/promotions/{promotionId}/academic-years")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .requestMatchers(HttpMethod.POST, "/academic-years/{academicYearId}/publish")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/semesters")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/students")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/students/{studentId}")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .requestMatchers(HttpMethod.GET, "/students/{studentId}/grades")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .requestMatchers(HttpMethod.GET, "/students/{studentId}/transcript")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .requestMatchers(HttpMethod.POST, "/students/{studentId}/transcript-emails")
                    .hasAnyRole("ADMIN", "STUDENT")
                    .requestMatchers(HttpMethod.POST, "/courses")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/course-assignments")
                    .hasRole("ADMIN")
                    .requestMatchers(
                        HttpMethod.POST, "/course-assignments/{courseAssignmentId}/exams")
                    .hasAnyRole("ADMIN", "TEACHER")
                    .requestMatchers(HttpMethod.PUT, "/exams/{examId}/grades/{studentId}")
                    .hasAnyRole("ADMIN", "TEACHER")
                    .requestMatchers(HttpMethod.GET, "/grades/{gradeId}/history")
                    .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                    (request, response, authException) -> response.sendError(401, "Unauthorized")))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
