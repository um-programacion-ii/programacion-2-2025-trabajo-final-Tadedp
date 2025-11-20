package com.example.backend.modules.eventos.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Integrante {

    private Long id;
    private String nombre;
    private String apellido;
    private String identificacion;
}
