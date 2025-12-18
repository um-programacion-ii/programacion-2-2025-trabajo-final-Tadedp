package com.example.backend.modules.ventas.domain.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venta {

    private Long id;
    private Long catedraVentaId;
    private Instant fechaVenta;
    private double precioVenta;
    private boolean resultado;
    private String descripcion;
    private String estado;
    private int intentos;
    private Long usuarioId;
    private Long eventoId;
    private Set<AsientoVendido> asientosVendidos = new HashSet<>();
}