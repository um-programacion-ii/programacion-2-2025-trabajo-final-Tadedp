package com.example.proxy.infrastructure.persistence.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsientoRepositoryAdapter {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Map<Object, Object> obtenerEstadoAsientos(Long eventoId) {
        String key = "evento_" + eventoId;

        String resultadoJson = redisTemplate.opsForValue().get(key);

        if (resultadoJson == null || resultadoJson.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            RedisEvento evento = objectMapper.readValue(resultadoJson, RedisEvento.class);

            if (evento.asientos() == null || evento.asientos().isEmpty()) {
                return Collections.emptyMap();
            }

            Map<Object, Object> resultadoMap = new HashMap<>();

            for (RedisAsiento asiento : evento.asientos()) {
                String mapKey = asiento.fila() + "-" + asiento.columna();
                String mapValue = asiento.estado();

                resultadoMap.put(mapKey, mapValue);
            }

            return resultadoMap;

        } catch (Exception e) {
            log.error("Error al parsear JSON de Redis para evento {}: {}", eventoId, e.getMessage());
            return Collections.emptyMap();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record RedisAsiento(
            int fila,
            int columna,
            String estado,
            String expira
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record RedisEvento(
            Long eventoId,
            List<RedisAsiento> asientos
    ) {}
}
