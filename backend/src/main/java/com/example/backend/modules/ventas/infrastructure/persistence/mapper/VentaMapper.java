package com.example.backend.modules.ventas.infrastructure.persistence.mapper;

import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import com.example.backend.modules.usuarios.infrastructure.persistence.entity.UsuarioEntity;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.Venta;
import com.example.backend.modules.ventas.infrastructure.persistence.entity.AsientoVendidoEntity;
import com.example.backend.modules.ventas.infrastructure.persistence.entity.VentaEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VentaMapper {
    public VentaEntity toEntity(Venta venta) {
        if (venta == null) {
            return null;
        }

        VentaEntity ventaEntity = new VentaEntity();
        ventaEntity.setId(venta.getId());
        ventaEntity.setCatedraVentaId(venta.getCatedraVentaId());
        ventaEntity.setFechaVenta(venta.getFechaVenta());
        ventaEntity.setPrecioVenta(venta.getPrecioVenta());
        ventaEntity.setResultado(venta.isResultado());
        ventaEntity.setDescripcion(venta.getDescripcion());
        ventaEntity.setEstado(venta.getEstado());
        ventaEntity.setIntentos(venta.getIntentos());

        if (venta.getUsuarioId() != null) {
            UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setId(venta.getUsuarioId());
            ventaEntity.setUsuario(usuarioEntity);
        }

        if (venta.getEventoId() != null) {
            EventoEntity eventoEntity = new EventoEntity();
            eventoEntity.setId(venta.getEventoId());
            ventaEntity.setEvento(eventoEntity);
        }

        if (venta.getAsientosVendidos() != null) {
             Set<AsientoVendidoEntity> asientosVendidosEntities = venta.getAsientosVendidos().stream()
                    .map(asientoVendido -> {
                        AsientoVendidoEntity asientoVendidoEntity = new AsientoVendidoEntity();
                        asientoVendidoEntity.setId(asientoVendido.getId());
                        asientoVendidoEntity.setFila(asientoVendido.getFila());
                        asientoVendidoEntity.setColumna(asientoVendido.getColumna());
                        asientoVendidoEntity.setPersona(asientoVendido.getPersona());
                        asientoVendidoEntity.setVenta(ventaEntity);
                        return asientoVendidoEntity;
                    }).collect(Collectors.toSet());
            ventaEntity.setAsientosVendidos(asientosVendidosEntities);
        }

        return ventaEntity;
    }

    public Venta toDomain(VentaEntity ventaEntity) {
        if (ventaEntity == null) {
            return null;
        }

        Venta venta = new Venta();
        venta.setId(ventaEntity.getId());
        venta.setCatedraVentaId(ventaEntity.getCatedraVentaId());
        venta.setFechaVenta(ventaEntity.getFechaVenta());
        venta.setPrecioVenta(ventaEntity.getPrecioVenta());
        venta.setResultado(ventaEntity.isResultado());
        venta.setDescripcion(ventaEntity.getDescripcion());
        venta.setEstado(ventaEntity.getEstado());
        venta.setIntentos(ventaEntity.getIntentos());

        if (ventaEntity.getUsuario() != null) {
            venta.setUsuarioId(ventaEntity.getUsuario().getId());
        }

        if (ventaEntity.getEvento() != null) {
            venta.setEventoId(ventaEntity.getEvento().getId());
        }

        if (ventaEntity.getAsientosVendidos() != null) {
            venta.setAsientosVendidos(ventaEntity.getAsientosVendidos().stream()
                    .map(asientoVendidoEntity -> {
                        AsientoVendido asientoVendido = new AsientoVendido();
                        asientoVendido.setId(asientoVendidoEntity.getId());
                        asientoVendido.setFila(asientoVendidoEntity.getFila());
                        asientoVendido.setColumna(asientoVendidoEntity.getColumna());
                        asientoVendido.setPersona(asientoVendidoEntity.getPersona());
                        asientoVendido.setVentaId(asientoVendidoEntity.getVenta().getId());
                        return asientoVendido;
                    }).collect(Collectors.toSet()));
        }

        return venta;
    }
}
