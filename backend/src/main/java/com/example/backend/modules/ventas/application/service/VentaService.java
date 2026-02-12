package com.example.backend.modules.ventas.application.service;

import com.example.backend.modules.compartido.domain.ports.out.CatedraIntegracion;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.domain.ports.out.SesionUsuarioRepository;
import com.example.backend.modules.ventas.domain.ports.out.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VentaService {
    private final VentaRepository ventaRepository;
    private final SesionUsuarioRepository sesionUsuarioRepository;
    private final CatedraIntegracion catedraIntegracion;

    public boolean bloquearAsientos(Long eventoId, Set<AsientoVendido> asientos) {
        return catedraIntegracion.bloquearAsientos(eventoId, asientos);
    }

    @Transactional
    public Venta confirmarVenta(Venta venta) {
        venta.setFechaVenta(Instant.now());
        venta.setIntentos(1);
        venta.setResultado(false);


        try {
            Long catedraId = catedraIntegracion.confirmarVenta(venta);

            if (catedraId != null) {
                venta.setCatedraVentaId(catedraId);
                venta.setEstado("CONFIRMADA");
                venta.setResultado(true);
                venta.setDescripcion("Venta realizada con éxito");
            } else {
                venta.setEstado("RECHAZADA");
                venta.setDescripcion("Rechazada por la cátedra");
            }

        } catch (Exception e) {
            venta.setEstado("PENDIENTE_REINTENTO");
            venta.setDescripcion("Venta pendiente de realizar");
        }

        sesionUsuarioRepository.deleteByUsuarioId(venta.getUsuarioId());
        return ventaRepository.save(venta);
    }

    @Transactional(readOnly = true)
    public List<Venta> obtenerHistoricoVentas(Long usuarioId) {
        return ventaRepository.findAllByUsuarioId(usuarioId);
    }
}
