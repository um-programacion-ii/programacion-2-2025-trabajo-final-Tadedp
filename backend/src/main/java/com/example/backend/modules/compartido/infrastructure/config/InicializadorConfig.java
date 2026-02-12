package com.example.backend.modules.compartido.infrastructure.config;

import com.example.backend.modules.eventos.application.service.EventoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InicializadorConfig {
    private final EventoService eventoService;

    @Bean
    public CommandLineRunner inicializacion() {
        return args -> {
            log.info("Iniciando sincronización inicial de eventos con la Cátedra...");
            try {
                eventoService.sincronizarEventos();
                log.info("Sincronización inicial completada.");
            } catch (Exception e) {
                log.error("Falló la sincronización inicial (puede que la cátedra esté offline). El sistema seguirá funcionando con datos locales o esperará a Kafka.", e);
            }
        };
    }
}
