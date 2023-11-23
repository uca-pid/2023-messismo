package com.messismo.bar.Services;

import com.messismo.bar.DTOs.PasswordRecoveryDTO;
import com.messismo.bar.Entities.PasswordRecovery;
import com.messismo.bar.Entities.User;
import com.messismo.bar.Exceptions.NoPinCreatedForUserException;
import com.messismo.bar.Exceptions.PinExpiredException;
import com.messismo.bar.Exceptions.UserNotFoundException;
import com.messismo.bar.Repositories.PasswordRecoveryRepository;
import com.messismo.bar.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
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


    public String forgotPassword(String email) throws Exception {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("No user has that email"));
            Optional<PasswordRecovery> passwordRecovery = passwordRecoveryRepository.findByUser(user);
            passwordRecovery.ifPresent(passwordRecoveryRepository::delete);
            String pin = generateRandomPin();
            Date dateCreated = new Date();
            PasswordRecovery newPasswordRecovery = new PasswordRecovery(pin, user, dateCreated);
            passwordRecoveryRepository.save(newPasswordRecovery);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom("automaticmoebar@hotmail.com");
            simpleMailMessage.setSubject("Recover your password, " + user.getFunctionalUsername());
            simpleMailMessage.setText("Your PIN is valid for 1 HOUR. Your PIN is: " + pin);
            javaMailSender.send(simpleMailMessage);
            return "Email sent!";
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT recover the password at the moment");
        }
    }

    public String changeForgottenPassword(PasswordRecoveryDTO passwordRecoveryDTO) throws Exception {
        try {
            Date dateCreated = new Date();
            User user = userRepository.findByEmail(passwordRecoveryDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("No user has that email"));
            PasswordRecovery passwordRecovery = passwordRecoveryRepository.findByUser(user).orElseThrow(() -> new NoPinCreatedForUserException("The user has no PINs created"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(passwordRecovery.getDateCreated());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date passwordRecoveryDatePlusOneHour = calendar.getTime();
            if (dateCreated.before(passwordRecoveryDatePlusOneHour) && Objects.equals(passwordRecoveryDTO.getPin(), passwordRecovery.getPin())) {
                user.updatePassword(passwordEncoder.encode(passwordRecoveryDTO.getNewPassword()));
                userRepository.save(user);
                passwordRecoveryRepository.delete(passwordRecovery);
                return "Password changed successfully";
            } else {
                throw new PinExpiredException("PIN expired!");
            }
        } catch (UserNotFoundException | NoPinCreatedForUserException | PinExpiredException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("CANNOT change the new password at the moment");
        }
    }
}