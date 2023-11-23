package com.messismo.bar.Services;

import com.messismo.bar.DTOs.AuthenticationRequestDTO;
import com.messismo.bar.DTOs.AuthenticationResponseDTO;
import com.messismo.bar.DTOs.RegisterRequestDTO;
import com.messismo.bar.Entities.Token;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.UserAlreadyExistsException;
import com.messismo.bar.Repositories.TokenRepository;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) throws Exception {
        try {
            Optional<User> employeeByUsername = userRepository.findByUsername(request.getUsername());
            Optional<User> employeeByMail = userRepository.findByEmail(request.getEmail());
            if (employeeByUsername.isPresent() || employeeByMail.isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            } else {
                User newEmployee = new User(request.getUsername(), request.getEmail(), request.getPassword());
                userRepository.save(newEmployee);
                String jwtToken = jwtService.generateToken(newEmployee);
                String refreshToken = jwtService.generateRefreshToken(newEmployee);
                saveUserToken(newEmployee, jwtToken);
                return new AuthenticationResponseDTO(jwtToken, refreshToken, newEmployee.getEmail(), newEmployee.getRole());
            }
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("User CANNOT be created");
        }
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return new AuthenticationResponseDTO(jwtToken, refreshToken, user.getEmail(), user.getRole());
        } catch (Exception e) {
            throw new Exception("Invalid user credentials");
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token(jwtToken, user);
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.removeValidation();
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
