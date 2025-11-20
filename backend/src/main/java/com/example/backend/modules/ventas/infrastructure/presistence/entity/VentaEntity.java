package com.example.backend.modules.ventas.infrastructure.presistence.entity;

import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import com.example.backend.modules.usuarios.infrastructure.persistence.entity.UsuarioEntity;
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
@Table(name = "ventas")
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long catedraVentaId;

    @Column(nullable = false)
    private LocalDate fechaVenta;

    @Column(nullable = false)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private boolean resultado;

    private String descripcion;

    @Column(nullable = false)
    private String estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private EventoEntity evento;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AsientoVendidoEntity> asientosVendidos = new HashSet<>();
}
