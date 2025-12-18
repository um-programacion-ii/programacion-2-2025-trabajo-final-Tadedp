package com.example.backend.modules.ventas.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AsientoVendidoRequest {
    @NotBlank
    private int fila;

    @NotBlank
    private int columna;

    private String persona;
}
