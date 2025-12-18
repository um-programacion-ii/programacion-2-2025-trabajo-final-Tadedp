package com.example.backend.modules.ventas.infrastructure.web.mapper;

import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.example.backend.modules.ventas.domain.model.SesionUsuario;
import com.example.backend.modules.ventas.infrastructure.web.dto.SesionUsuarioRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SesionUsuarioDtoMapper {
    public SesionUsuario toDomain(SesionUsuarioRequest sesionUsuarioRequest) {
        if (sesionUsuarioRequest == null) {
            return null;
        }

        SesionUsuario sesionUsuario = new SesionUsuario();
        sesionUsuario.setEventoId(sesionUsuarioRequest.getEventoId());
        sesionUsuario.setPasoActual(sesionUsuarioRequest.getPasoActual());
        sesionUsuario.setAsientosSeleccionados(sesionUsuarioRequest.getAsientosSeleccionados().stream()
                .map(asientoVendidoRequest -> {
                    AsientoVendido asientoVendido = new AsientoVendido();
                    asientoVendido.setFila(asientoVendidoRequest.getFila());
                    asientoVendido.setColumna(asientoVendidoRequest.getColumna());
                    asientoVendido.setPersona(asientoVendidoRequest.getPersona());
                    return asientoVendido;
                }).collect(Collectors.toList()));

        return sesionUsuario;
    }
}
