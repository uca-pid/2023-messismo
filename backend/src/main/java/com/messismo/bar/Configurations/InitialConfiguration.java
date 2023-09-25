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


@Configuration
@RequiredArgsConstructor
public class InitialConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService, UserRepository userRepository) {
        return args -> {
            RegisterRequestDTO admin = new RegisterRequestDTO();
            admin.setUsername("admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword("Password1");
            authenticationService.register(admin);
            User createdAdmin = userRepository.findByEmail(admin.getEmail()).get();
            createdAdmin.setRole(Role.ADMIN);
            userRepository.save(createdAdmin);
        };
    }

}
