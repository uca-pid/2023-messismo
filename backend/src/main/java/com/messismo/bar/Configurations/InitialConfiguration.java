package com.messismo.bar.Configurations;

import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import com.messismo.bar.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;


@Configuration
@RequiredArgsConstructor
public class InitialConfiguration {

    private final UserDetailsService userService;
//    @Bean
//    CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncode) {
//        return args -> {
//            if (userRepository.findByUsername("admin").isPresent()) {
//                return;
//            }
//            User admin = new User(1L, "admin","admin@gmail.com", passwordEncode.encode("password"), Role.SUPER_ADMIN);
//            userRepository.save(admin);
//        };
//    }
@Bean
public CommandLineRunner commandLineRunner(
        AuthenticationService service,UserRepository userRepository
) {
    return args -> {
        RegisterRequestDTO admin = new RegisterRequestDTO();
        admin.setUsername("admin");
        admin.setEmail("admin@mail.com");
        admin.setPassword("password");
        service.register(admin);
        User createdAdmin = userRepository.findByEmail(admin.getEmail()).get();
        createdAdmin.setRole(Role.ADMIN);
        userRepository.save(createdAdmin);
    };
}

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


}
