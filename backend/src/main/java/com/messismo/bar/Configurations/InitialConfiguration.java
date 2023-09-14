package com.messismo.bar.Configurations;

import com.messismo.bar.Entities.Menu;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.MenuRepository;
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
            User admin = new User(0L, "admin", passwordEncode.encode("password"), Role.ADMIN);
            userRepository.save(admin);
        };
    }

    @Bean
    CommandLineRunner run1(MenuRepository menuRepository) {
        return args -> {
            if (menuRepository.findByMenuId(1L).isPresent()) {
                return;
            }
            Menu menu = new Menu(1L, "Menu1", new HashSet<>());
            menuRepository.save(menu);
        };
    }


}
