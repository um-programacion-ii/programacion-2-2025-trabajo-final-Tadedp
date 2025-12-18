package com.example.backend.modules.ventas.infrastructure.persistence.repository;

import com.example.backend.modules.ventas.infrastructure.persistence.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaVentaRepository extends JpaRepository<VentaEntity, Long> {
    List<VentaEntity> findByEstado(String estado);
    List<VentaEntity> findByUsuarioId(Long usuarioId);
}
