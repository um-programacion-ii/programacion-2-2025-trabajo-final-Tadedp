package com.example.backend.modules.ventas.application.service;

import com.example.backend.modules.compartido.domain.ports.out.CatedraIntegracion;
import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.domain.ports.out.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReintentoVentaService {
    private final VentaRepository ventaRepository;
    private final CatedraIntegracion catedraIntegracion;

    @Scheduled(fixedDelay = 500000)
    @Transactional
    public void processPendingSales() {
        List<Venta> ventasPendientes = ventaRepository.findAllByEstado("PENDIENTE_REINTENTO");

        for (Venta venta : ventasPendientes) {
            if (venta.getIntentos() >= 3) {
                venta.setEstado("FALLIDA");
                ventaRepository.save(venta);
                continue;
            }

            try {
                log.info("Reintentando venta ID: {}", venta.getId());
                Long catedraId = catedraIntegracion.confirmarVenta(venta);

                if (catedraId != null) {
                    venta.setCatedraVentaId(catedraId);
                    venta.setEstado("CONFIRMADA");
                    venta.setResultado(true);
                    venta.setDescripcion("Venta realizada con éxito tras reintento");
                } else {
                    venta.setIntentos(venta.getIntentos() + 1);
                }
            } catch (Exception e) {
                venta.setIntentos(venta.getIntentos() + 1);
            }
            ventaRepository.save(venta);
        }
    }
}
