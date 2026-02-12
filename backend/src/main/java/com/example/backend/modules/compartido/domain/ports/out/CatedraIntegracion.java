package com.example.backend.modules.compartido.domain.ports.out;

import com.example.backend.modules.eventos.domain.model.Evento;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.Venta;

import java.util.List;
import java.util.Set;

public interface CatedraIntegracion {
    List<Evento> obtenerEventos();
    boolean bloquearAsientos(Long eventoId, Set<AsientoVendido> asientos);
    Long confirmarVenta(Venta venta);
}
