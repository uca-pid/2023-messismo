package com.messismo.bar.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.messismo.bar.Entities.Role.*;

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
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**")).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/admin/**")).hasRole(ADMIN.name());
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/manager/**")).hasAnyRole(ADMIN.name(),
                    MANAGER.name());
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/validatedEmployee/**")).hasAnyRole(ADMIN.name(),
                    MANAGER.name(), VALIDATEDEMPLOYEE.name());
            auth.requestMatchers(new AntPathRequestMatcher("/api/v1/employee/**")).hasAnyRole(ADMIN.name(),
                    MANAGER.name(), VALIDATEDEMPLOYEE.name(), EMPLOYEE.name());
            auth.requestMatchers(PathRequest.toH2Console()).permitAll(); // SOLO SI SE NECESITA LA CONSOLA DE LA H2, ES
                                                                         // DECIR, ESTA CON EL PROFILE TEST
            auth.anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> {
                    logout.logoutUrl("/api/v1/auth/logout");
                    logout.addLogoutHandler(logoutHandler);
                    logout.logoutSuccessHandler(
                            (request, response, authentication) -> SecurityContextHolder.clearContext());
                });
        http.headers(
                headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    ////////////////// EL DE ABAJO ANDA//////////////////////////
    // http
    // .csrf()
    // .disable()
    // .authorizeHttpRequests()
    // .requestMatchers("/api/v1/auth/**")
    // .permitAll()
    // .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
    // .requestMatchers("/api/v1/manager/**").hasAnyRole(ADMIN.name(),
    // MANAGER.name())
    // .requestMatchers("/api/v1/validatedEmployee/**").hasAnyRole(ADMIN.name(),
    // MANAGER.name(), VALIDATEDEMPLOYEE.name())
    // .requestMatchers("/api/v1/employee/**").hasAnyRole(ADMIN.name(),
    // MANAGER.name(), VALIDATEDEMPLOYEE.name(), EMPLOYEE.name())
    // .anyRequest()
    // .authenticated()
    // .and()
    // .sessionManagement()
    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .and()
    // .authenticationProvider(authenticationProvider)
    // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
    // .logout()
    // .logoutUrl("/api/v1/auth/logout")
    // .addLogoutHandler(logoutHandler)
    // .logoutSuccessHandler((request, response, authentication) ->
    // SecurityContextHolder.clearContext())
    // ;
    ////////////////////// EL DE ARRIBA ANDA//////////////////////////////
    // return http.build();

}