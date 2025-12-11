package com.example.backend.modules.ventas.infrastructure.presistence.repository;

import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.domain.ports.out.VentaRepository;
import com.example.backend.modules.ventas.infrastructure.presistence.entity.VentaEntity;
import com.example.backend.modules.ventas.infrastructure.presistence.mapper.VentaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaVentaRepositoryAdapter implements VentaRepository {
    private final JpaVentaRepository jpaVentaRepository;
    private final VentaMapper ventaMapper;

    @Override
    public Optional<Venta> findById(Long id) {
        return jpaVentaRepository.findById(id).map(ventaMapper::toDomain);
    }

    @Override
    public Venta save(Venta venta) {
        VentaEntity ventaEntity = ventaMapper.toEntity(venta);
        VentaEntity savedVenta = jpaVentaRepository.save(ventaEntity);
        return ventaMapper.toDomain(savedVenta);
    }

    @Override
    public List<Venta> findAllByUsuarioId(Long usuarioId) {
        return jpaVentaRepository.findByUsuarioId(usuarioId).stream()
                .map(ventaMapper::toDomain)
                .collect(Collectors.toList());
    }
}
