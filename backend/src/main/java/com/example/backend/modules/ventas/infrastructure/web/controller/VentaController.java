package com.example.backend.modules.ventas.infrastructure.web.controller;

import com.example.backend.modules.compartido.infrastructure.security.CustomUserDetails;
import com.example.backend.modules.ventas.application.service.VentaService;
import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.infrastructure.web.dto.VentaRequest;
import com.example.backend.modules.ventas.infrastructure.web.mapper.VentaDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;
    private final VentaDtoMapper ventaDtoMapper;

    @PostMapping("/bloquear")
    public ResponseEntity<?> bloquearAsientos(@RequestBody VentaRequest request) {
        Venta venta = ventaDtoMapper.toDomain(request);

        boolean resultado = ventaService.bloquearAsientos(venta.getEventoId(), venta.getAsientosVendidos());

        if (resultado) {
            return ResponseEntity.ok().body("{\"mensaje\": \"Asientos bloqueados\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"No se pudieron bloquear los asientos\"}");
        }
    }

    @PostMapping("/comprar")
    public ResponseEntity<?> realizarCompra(@RequestBody VentaRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Venta venta = ventaDtoMapper.toDomain(request);
        venta.setUsuarioId(userDetails.getId());

        Venta ventaProcesada = ventaService.confirmarVenta(venta);

        if (ventaProcesada.isResultado()) {
            return ResponseEntity.ok(ventaProcesada);
        } else if ("PENDIENTE_REINTENTO".equals(ventaProcesada.getEstado())) {
            return ResponseEntity.accepted().body(ventaProcesada);
        } else {
            return ResponseEntity.badRequest().body(ventaProcesada);
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Venta>> verHistorial(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ventaService.obtenerHistoricoVentas(userDetails.getId()));
    }
}
