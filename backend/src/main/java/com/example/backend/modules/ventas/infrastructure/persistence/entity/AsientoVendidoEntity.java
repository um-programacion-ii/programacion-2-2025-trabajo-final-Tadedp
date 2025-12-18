package com.example.backend.modules.ventas.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "asientos_vendidos")
public class AsientoVendidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int fila;

    @Column(nullable = false)
    private int columna;

    @Column(nullable = false)
    private String persona;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VentaEntity venta;
}
