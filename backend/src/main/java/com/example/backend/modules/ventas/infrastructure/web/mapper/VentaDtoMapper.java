package com.example.backend.modules.ventas.infrastructure.web.mapper;

import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.infrastructure.web.dto.VentaRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class VentaDtoMapper {
    public Venta toDomain(VentaRequest ventaRequest) {
        if  (ventaRequest == null) {
            return null;
        }

        Venta venta = new Venta();
        venta.setEventoId(ventaRequest.getEventoId());
        venta.setPrecioVenta(ventaRequest.getPrecioVenta());
        venta.setAsientosVendidos(ventaRequest.getAsientosVendidos().stream()
                .map(asientoVendidoRequest -> {
                    AsientoVendido asientoVendido = new AsientoVendido();
                    asientoVendido.setFila(asientoVendidoRequest.getFila());
                    asientoVendido.setColumna(asientoVendidoRequest.getColumna());
                    if (asientoVendidoRequest.getPersona() != null) {
                        asientoVendido.setPersona(asientoVendidoRequest.getPersona());
                    }
                    return asientoVendido;
                }).collect(Collectors.toSet()));

        return venta;
    }
}
