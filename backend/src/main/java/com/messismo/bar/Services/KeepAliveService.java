package com.messismo.bar.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log
public class KeepAliveService {

    private final RestTemplate restTemplate;


    // Execute every 2 minutes (120000ms)
    @Scheduled(fixedRate = 120000) // CADA DOS MINUTOS
    public void keepBackendAlive() {
        ResponseEntity<String> response = restTemplate.getForEntity("https://backendmessismo.onrender.com/api/v1/auth/health", String.class);
        log.info("Server response: " + response.getBody());
    }
}