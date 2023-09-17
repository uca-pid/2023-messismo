package com.messismo.bar.Configurations;

import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;


@Configuration
public class InitialConfiguration {
    @Bean
    CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncode) {
        return args -> {
            if (userRepository.findByUsername("admin").isPresent()) {
                return;
            }
            User admin = new User(1L, "admin","admin@gmail.com", passwordEncode.encode("password"), Role.SUPER_ADMIN);
            userRepository.save(admin);
        };
    }


}
