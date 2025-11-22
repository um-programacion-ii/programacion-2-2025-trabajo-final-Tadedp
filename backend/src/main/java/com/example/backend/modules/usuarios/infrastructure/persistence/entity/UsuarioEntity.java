package com.example.backend.modules.usuarios.infrastructure.persistence.entity;

import com.example.backend.modules.ventas.infrastructure.presistence.entity.VentaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name="password_hash", nullable = false)
    private String password;

    private String nombre;

    private String apellido;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "usuario")
    private Set<VentaEntity> ventas = new HashSet<>();
}
