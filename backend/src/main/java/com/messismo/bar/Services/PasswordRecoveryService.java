package com.messismo.bar.Services;

import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.Entities.PasswordRecovery;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Repositories.PasswordRecoveryRepository;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final UserRepository userRepository;

    private final PasswordRecoveryRepository passwordRecoveryRepository;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    public static String generateRandomPin() {

        String validChars = "0123456789";
        StringBuilder pinBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(validChars.length());
            char randomDigit = validChars.charAt(randomIndex);
            pinBuilder.append(randomDigit);
        }
        return pinBuilder.toString();
    }


    public ResponseEntity<String> forgotPassword(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("No user has that email."));
            Optional<PasswordRecovery> passwordRecovery = passwordRecoveryRepository.findByUser(user);
            passwordRecovery.ifPresent(passwordRecoveryRepository::delete);
            String pin = generateRandomPin();
            Date dateCreated = new Date();
            PasswordRecovery newPasswordRecovery = new PasswordRecovery(pin,user,dateCreated);
//            PasswordRecovery newPasswordRecovery = PasswordRecovery.builder().user(user).pin(pin).dateCreated(dateCreated).build();
            passwordRecoveryRepository.save(newPasswordRecovery);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom("automaticmoebar@hotmail.com");
            simpleMailMessage.setSubject("Recover your password, " + user.getFunctionalUsername());
            simpleMailMessage.setText("Your PIN is valid for 1 HOUR. Your PIN is: " + pin);
            javaMailSender.send(simpleMailMessage);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT recover the password at the moment");
        }
    }

    public ResponseEntity<String> changeForgottenPassword(PasswordRecoveryDTO passwordRecoveryDTO) {
        try {
            Date dateCreated = new Date();
            User user = userRepository.findByEmail(passwordRecoveryDTO.getEmail()).orElseThrow(() -> new Exception("No user has that email."));
            PasswordRecovery passwordRecovery = passwordRecoveryRepository.findByUser(user).orElseThrow(() -> new Exception("The user has no PINs created"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(passwordRecovery.getDateCreated());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date passwordRecoveryDatePlusOneHour = calendar.getTime();
            if (dateCreated.before(passwordRecoveryDatePlusOneHour) && Objects.equals(passwordRecoveryDTO.getPin(), passwordRecovery.getPin())) {
                user.updatePassword(passwordEncoder.encode(passwordRecoveryDTO.getNewPassword()));
//                user.setPassword(passwordEncoder.encode(passwordRecoveryDTO.getNewPassword()));
                userRepository.save(user);
                passwordRecoveryRepository.delete(passwordRecovery);
                return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("PIN expired!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CANNOT change the new password at the moment");
        }
    }
}