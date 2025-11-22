package com.example.backend.modules.usuarios.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo = true;
}