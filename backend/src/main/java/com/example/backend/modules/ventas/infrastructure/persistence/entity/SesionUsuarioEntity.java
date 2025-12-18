package com.example.backend.modules.ventas.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sesion_usuario")
public class SesionUsuarioEntity {
    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "evento_id")
    private Long eventoId;

    @Column(name = "paso_actual")
    private String pasoActual;

    @Lob
    @Column(name = "datos_temporales_json", columnDefinition = "TEXT")
    private String datosTemporalesJson;

    @Column(name = "ultima_modificacion")
    private Instant ultimaModificacion;
}
