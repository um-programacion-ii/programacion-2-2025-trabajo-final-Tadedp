package com.example.backend.modules.eventos.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eventos")
public class EventoEntity {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String resumen;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;

    private String direccion;

    @Column(columnDefinition = "TEXT")
    private String imagen;

    @Column(nullable = false)
    private int filaAsientos;

    @Column(nullable = false)
    private int columnAsientos;

    @Column(nullable = false)
    private BigDecimal precioEntrada;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tipo_evento_id")
    private TipoEventoEntity eventoTipo;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "evento_integrante",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "integrante_id")
    )
    private Set<IntegranteEntity> integrantes = new HashSet<>();
}
