package com.example.backend.modules.usuarios.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroRequest {
    @NotBlank()
    private String username;

    @NotBlank()
    private String password;

    @NotBlank()
    private String nombre;

    @NotBlank()
    private String apellido;

    @NotBlank
    private String email;
}
