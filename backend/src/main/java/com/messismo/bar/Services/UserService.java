package com.messismo.bar.Services;

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

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")) ;
    }


    public ResponseEntity<?> getAllEmployees() {
        try{
            List<User> users = userRepository.findAll();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(users);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT retrieve employees right now");
        }
    }

    public ResponseEntity<?> validateEmployee(Long userId) {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User DOES NOT exist"));
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            boolean isEmployee = false;
            for (GrantedAuthority authority : authorities) {
                if ("VALIDATED_EMPLOYEE".equals(authority.getAuthority())) {
                    isEmployee = true;
                    break;
                }
            }
            if (!isEmployee) {
                user.setRole(Role.VALIDATED_EMPLOYEE);
                return ResponseEntity.status(HttpStatus.OK).body("User IS NOW an employee");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User IS already an employee");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User DOES NOT exist");
        }
    }

}
