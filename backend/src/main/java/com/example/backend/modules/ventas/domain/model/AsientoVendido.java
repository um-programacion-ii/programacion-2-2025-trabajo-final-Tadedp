package com.example.backend.modules.ventas.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsientoVendido {

    private Long id;
    private int fila;
    private int columna;
    private String persona;
    private Long ventaId;
}
