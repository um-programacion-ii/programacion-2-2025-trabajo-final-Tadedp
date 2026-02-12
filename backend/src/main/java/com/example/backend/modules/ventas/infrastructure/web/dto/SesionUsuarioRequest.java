package com.example.backend.modules.ventas.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SesionUsuarioRequest {
    @NotBlank
    private Long eventoId;

    @NotBlank
    private String pasoActual;

    @NotBlank
    private List<AsientoVendidoRequest>  asientosSeleccionados;
}
