package com.messismo.bar.Services;

import com.messismo.bar.DTOs.UserDTO;
import com.messismo.bar.DTOs.UserIdDTO;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.CannotUpgradeToManager;
import com.messismo.bar.Exceptions.CannotUpgradeToValidatedEmployee;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<UserDTO> getAllEmployees() {
        List<User> allEmployees = userRepository.findAll();
        List<UserDTO> response = new ArrayList<>();
        for (User user : allEmployees) {
            UserDTO newUserDTO = UserDTO.builder().id(user.getId()).role(user.getRole()).username(user.getFunctionalUsername()).email(user.getEmail()).build();
            response.add(newUserDTO);
        }
        return response;
    }

    public String validateEmployee(UserIdDTO userIdDTO) throws Exception {
        try {
            User user = userRepository.findById(userIdDTO.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User DOES NOT exist"));
            if (user.isEmployee()) {
                user.upgradeToValidateEmployee();
                userRepository.save(user);
                return "User IS NOW a VALIDATED_EMPLOYEE";
            } else {
                throw new CannotUpgradeToValidatedEmployee("User IS already a VALIDATED_EMPLOYEE OR SUPERIOR");
            }
        } catch (UsernameNotFoundException | CannotUpgradeToValidatedEmployee e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Cannot upgrade to validated employee");
        }
    }

    public String validateManager(UserIdDTO userIdDTO) throws Exception {
        try {
            User user = userRepository.findById(userIdDTO.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User DOES NOT exist"));
            if (user.isValidatedEmployee()) {
                user.upgradeToManager();
                userRepository.save(user);
                return "User IS NOW a MANAGER";
            } else {
                throw new CannotUpgradeToManager("User MUST be first a VALIDATED_EMPLOYEE");
            }
        } catch (UsernameNotFoundException | CannotUpgradeToManager e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Cannot upgrade to manager");
        }
    }
}
