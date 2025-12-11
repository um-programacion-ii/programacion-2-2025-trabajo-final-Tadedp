package com.example.backend.modules.ventas.infrastructure.presistence.repository;

import com.example.backend.modules.ventas.infrastructure.presistence.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaVentaRepository extends JpaRepository<VentaEntity, Long> {
    List<VentaEntity> findByUsuarioId(Long usuarioId);
}
