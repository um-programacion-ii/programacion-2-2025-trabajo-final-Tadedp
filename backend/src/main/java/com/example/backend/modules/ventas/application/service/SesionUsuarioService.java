package com.example.backend.modules.ventas.application.service;

import com.example.backend.modules.ventas.domain.model.SesionUsuario;
import com.example.backend.modules.ventas.domain.ports.out.SesionUsuarioRepository;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionUsuarioService {
    private final SesionUsuarioRepository sesionUsuarioRepository;

    @Transactional(readOnly = true)
    public SesionUsuario obtenerSesion(Long usuarioId) {
        return sesionUsuarioRepository.findByUsuarioId(usuarioId)
                .orElse(new SesionUsuario(
                        usuarioId,
                        null,
                        "INICIO",
                        new ArrayList<>(),
                        Instant.now())
                );
    }

    @Transactional
    public void actualizarEstadoSesion(Long usuarioId, Long eventoId, String paso, List<AsientoVendido> asientos) {
        SesionUsuario sesionUsuario = new SesionUsuario();
        sesionUsuario.setUsuarioId(usuarioId);
        sesionUsuario.setEventoId(eventoId);
        sesionUsuario.setPasoActual(paso);
        sesionUsuario.setAsientosSeleccionados(asientos != null ? asientos : new ArrayList<>());
        sesionUsuario.setUltimaModificacion(Instant.now());

        sesionUsuarioRepository.save(sesionUsuario);
    }

    @Transactional
    public void eliminarSesion(Long usuarioId) {
        sesionUsuarioRepository.deleteByUsuarioId(usuarioId);
    }
}
