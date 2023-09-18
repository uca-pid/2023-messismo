package com.messismo.bar.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.messismo.bar.Entities.Role.*;


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
//            auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll();
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll();
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/superAdmin/**")).hasRole("SUPER_ADMIN");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/validatedAdmin/**")).hasAnyRole("SUPER_ADMIN","VALIDATED_ADMIN");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/validatedEmployee/**")).hasAnyRole("SUPER_ADMIN", "VALIDATED_ADMIN", "VALIDATED_EMPLOYEE");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/employee/**")).hasAnyRole("SUPER_ADMIN", "VALIDATED_ADMIN", "VALIDATED_EMPLOYEE", "EMPLOYEE");
//            auth.anyRequest().authenticated();
//        });
//        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        httpSecurity.authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.headers().frameOptions().disable();
//        return httpSecurity.build();
//    }

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/v1/admin/**")).hasRole(ADMIN.name())
            .requestMatchers(new AntPathRequestMatcher("/api/v1/manager/**")).hasAnyRole(ADMIN.name(), MANAGER.name())
            .requestMatchers(new AntPathRequestMatcher("/api/v1/validatedEmployee/**")).hasAnyRole(ADMIN.name(), MANAGER.name(), VALIDATEDEMPLOYEE.name())
            .requestMatchers(new AntPathRequestMatcher("/api/v1/employee/**")).hasAnyRole(ADMIN.name(), MANAGER.name(), VALIDATEDEMPLOYEE.name(), EMPLOYEE.name())
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}