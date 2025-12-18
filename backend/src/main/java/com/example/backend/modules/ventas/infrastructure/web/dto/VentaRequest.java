package com.example.backend.modules.ventas.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class VentaRequest {
    @NotBlank
    private Long eventoId;

    @NotBlank
    private double precioVenta;

    @NotBlank
    private List<AsientoVendidoRequest> asientosVendidos;
}
