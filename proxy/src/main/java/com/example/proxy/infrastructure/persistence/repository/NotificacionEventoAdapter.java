package com.example.proxy.infrastructure.persistence.repository;

import com.example.proxy.infrastructure.client.BackendClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionEventoAdapter {
    private final BackendClient backendClient;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.consumer.group-id}")
    public void listen(String message) {
        log.info("Mensaje recibido de Kafka: {}", message);

        try {
            backendClient.notificarCambio();
            log.info("Backend notificado exitosamente.");
        } catch (Exception e) {
            log.error("Error al notificar al backend: {}", e.getMessage());
        }
    }
}
