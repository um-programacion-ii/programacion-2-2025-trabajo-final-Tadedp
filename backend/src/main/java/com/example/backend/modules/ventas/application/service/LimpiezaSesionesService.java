package com.example.backend.modules.ventas.application.service;

import com.example.backend.modules.ventas.domain.ports.out.SesionUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class LimpiezaSesionesService {
    private final SesionUsuarioRepository sesionUsuarioRepository;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public LimpiezaSesionesService(SesionUsuarioRepository sesionUsuarioRepository) {
        this.sesionUsuarioRepository = sesionUsuarioRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void limpiarSesionesExpiradas() {
        Instant umbral = Instant.now().minusMillis(expiration);
        sesionUsuarioRepository.deleteByFechaCreacionBefore(umbral);
    }
}
