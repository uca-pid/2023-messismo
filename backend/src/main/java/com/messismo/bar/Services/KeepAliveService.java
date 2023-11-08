package com.messismo.bar.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeepAliveService {

    private final RestTemplate restTemplate;


    // Execute every 2 minutes (120000ms)
    @Scheduled(fixedRate = 120000)
    public void keepBackendAlive() {
        String backendURL = "https://backendmessismo.onrender.com/api/v1/auth/health";
        ResponseEntity<String> response = restTemplate.getForEntity(backendURL, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println("Server response: " + responseBody);
        } else {
            System.err.println("Server response failed with code: " + response.getStatusCode());
        }
    }
}