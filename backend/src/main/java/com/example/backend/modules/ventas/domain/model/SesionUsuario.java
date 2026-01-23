package com.example.backend.modules.ventas.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SesionUsuario {

    private Long usuarioId;
    private Long eventoId;
    private String pasoActual;
    private List<AsientoVendido> asientosSeleccionados = new ArrayList<>();
    private Instant ultimaModificacion;
    private Instant fechaCreacion;
}
