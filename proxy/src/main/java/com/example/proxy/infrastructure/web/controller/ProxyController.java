package com.example.proxy.infrastructure.web.controller;

import com.example.proxy.infrastructure.persistence.repository.AsientoRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class ProxyController {
    private final AsientoRepositoryAdapter asientoRepositoryAdapter;

    @GetMapping("/{id}/asientos")
    public ResponseEntity<Map<String, String>> obtenerAsientos(@PathVariable Long id) {
        Map<Object, Object> rawData = asientoRepositoryAdapter.obtenerEstadoAsientos(id);

        Map<String, String> stringData = rawData.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue().toString()
                ));

        return ResponseEntity.ok(stringData);
    }
}
