package com.example.backend.modules.ventas.infrastructure.persistence.repository;

import com.example.backend.modules.ventas.infrastructure.persistence.entity.SesionUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface JpaSesionUsuarioRepository extends JpaRepository<SesionUsuarioEntity, Long> {
    void deleteByFechaCreacionBefore(Instant fechaLimite);
}
