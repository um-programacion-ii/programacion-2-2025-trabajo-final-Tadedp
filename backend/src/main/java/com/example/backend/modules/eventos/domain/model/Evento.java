package com.example.backend.modules.eventos.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    private Long id;
    private String titulo;
    private String resumen;
    private String descripcion;
    private LocalDate fecha;
    private String direccion;
    private String imagen;
    private int filaAsientos;
    private int columnAsientos;
    private BigDecimal precioEntrada;
    private TipoEvento tipoEvento;
    private Set<Integrante> integrantes = new HashSet<>();
}
