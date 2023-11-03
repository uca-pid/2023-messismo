package com.messismo.bar.Services;

import com.messismo.bar.DTOs.UserDTO;
import com.messismo.bar.Entities.Role;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<?> getAllEmployees() {
        List<User> allEmployees = userRepository.findAll();
        List<UserDTO> response = new ArrayList<>();
        for (User user : allEmployees) {
            UserDTO newUserDTO = UserDTO.builder().id(user.getId()).role(user.getRole()).username(user.getFunctionalUsername()).email(user.getEmail()).build();
            response.add(newUserDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> validateEmployee(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User DOES NOT exist"));
//            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
//            boolean isEmployee = false;
//            boolean isValidEmployee= user.isValidatedEmployeeOrGreater();
//            for (GrantedAuthority authority : authorities) {
//                if ("ROLE_VALIDATEDEMPLOYEE".equals(authority.getAuthority()) || "ROLE_MANAGER".equals(authority.getAuthority()) || "ROLE_ADMIN".equals(authority.getAuthority())) {
//                    isEmployee = true;
//                    break;
//                }
//            }
            if (user.isEmployee()) {
                user.upgradeToValidateEmployee();
//                user.setRole(Role.VALIDATEDEMPLOYEE);
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("User IS NOW a VALIDATED_EMPLOYEE");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User IS already a VALIDATED_EMPLOYEE OR SUPERIOR");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");
        }
    }

    public ResponseEntity<?> validateManager(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User DOES NOT exist"));
//            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
//            boolean isValidatedEmployee = false;
//            for (GrantedAuthority authority : authorities) {
//                if ("ROLE_VALIDATEDEMPLOYEE".equals(authority.getAuthority())) {
//                    isValidatedEmployee = true;
//                    break;
//                }
//            }
//            boolean isValidatedEmployee = user.isValidatedEmployee();
            if (user.isValidatedEmployee()) {
                user.upgradeToManager();
//                user.setRole(Role.MANAGER);
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("User IS NOW a MANAGER");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User MUST be first a VALIDATED_EMPLOYEE");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");
        }
    }
}
