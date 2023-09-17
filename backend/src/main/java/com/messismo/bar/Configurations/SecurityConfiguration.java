package com.messismo.bar.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;
    private UserDetailsService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> {
            auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/superAdmin/**")).hasRole("SUPER_ADMIN");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/validatedAdmin/**")).hasAnyRole("SUPER_ADMIN","VALIDATED_ADMIN");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/validatedEmployee/**")).hasAnyRole("SUPER_ADMIN", "VALIDATED_ADMIN", "VALIDATED_EMPLOYEE");
//            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/employee/**")).hasAnyRole("SUPER_ADMIN", "VALIDATED_ADMIN", "VALIDATED_EMPLOYEE", "EMPLOYEE");
//            auth.anyRequest().authenticated();
        });


        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().frameOptions().disable();
        return httpSecurity.build();
    }





    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
