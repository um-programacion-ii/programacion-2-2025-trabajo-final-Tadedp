package com.example.proxy.infrastructure.client;

import com.example.proxy.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BackendClient {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${app.backend.url}")
    private String backendUrl;

    public void notificarCambio() {
        String backendToken = jwtUtil.generarToken("PROXY_SERVICE", 0L);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + backendToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    backendUrl,
                    HttpMethod.POST,
                    request,
                    Void.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
