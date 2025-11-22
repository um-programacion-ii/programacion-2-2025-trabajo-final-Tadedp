package com.example.backend.modules.eventos.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEvento {

    private Long id;
    private String nombre;
    private String descripcion;
}
