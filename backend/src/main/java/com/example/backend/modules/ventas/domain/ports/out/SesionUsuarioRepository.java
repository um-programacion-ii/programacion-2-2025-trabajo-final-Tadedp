package com.example.backend.modules.ventas.domain.ports.out;

import com.example.backend.modules.ventas.domain.model.SesionUsuario;

import java.util.Optional;

public interface SesionUsuarioRepository {
    SesionUsuario save(SesionUsuario session);
    Optional<SesionUsuario> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}
