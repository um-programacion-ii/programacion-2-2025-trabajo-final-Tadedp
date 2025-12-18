package com.example.backend.modules.eventos.infrastructure.client;

import com.example.backend.modules.compartido.infrastructure.security.JwtUtil;
import com.example.backend.modules.eventos.domain.ports.out.ProxyIntegracion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProxyRestAdapter implements ProxyIntegracion {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${app.proxy.url}")
    private String proxyUrl;

    @Override
    public Map<String, String> obtenerEstadoAsientos(Long eventoId) {
        String url = proxyUrl + "/eventos/" + eventoId + "/asientos";

        String proxyToken = jwtUtil.generarToken("BACKEND_SERVICE", 0L);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + proxyToken);
            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<Map<String, String>>() {}
            );

            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
