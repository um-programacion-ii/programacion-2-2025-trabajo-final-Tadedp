package com.example.backend.modules.ventas.domain.ports.out;

import com.example.backend.modules.ventas.domain.model.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaRepository {
    Optional<Venta> findById(Long id);
    Venta save(Venta venta);
    List<Venta> findAllByUsuarioId(Long usuarioId);
}
