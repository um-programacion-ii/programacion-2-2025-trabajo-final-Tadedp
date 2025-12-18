package com.example.backend.modules.ventas.infrastructure.persistence.mapper;

import com.example.backend.modules.ventas.domain.model.SesionUsuario;
import com.example.backend.modules.ventas.infrastructure.persistence.entity.SesionUsuarioEntity;
import com.example.backend.modules.ventas.domain.model.AsientoVendido;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SesionUsuarioMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SesionUsuarioEntity toEntity(SesionUsuario sesionUsuario) {
        if (sesionUsuario == null) {
            return null;
        }

        SesionUsuarioEntity sesionUsuarioEntity = new SesionUsuarioEntity();
        sesionUsuarioEntity.setUsuarioId(sesionUsuario.getUsuarioId());
        sesionUsuarioEntity.setEventoId(sesionUsuario.getEventoId());
        sesionUsuarioEntity.setPasoActual(sesionUsuario.getPasoActual());
        sesionUsuarioEntity.setUltimaModificacion(sesionUsuario.getUltimaModificacion());

        try {
            String json = objectMapper.writeValueAsString(sesionUsuario.getAsientosSeleccionados());
            sesionUsuarioEntity.setDatosTemporalesJson(json);
        } catch (Exception e) {
            e.printStackTrace();
            sesionUsuarioEntity.setDatosTemporalesJson("[]");
        }
        return sesionUsuarioEntity;
    }

    public SesionUsuario toDomain(SesionUsuarioEntity sesionUsuarioEntity) {
        List<AsientoVendido > asientos = new ArrayList<>();

        try {
            if (sesionUsuarioEntity.getDatosTemporalesJson() != null) {
                asientos = objectMapper.readValue(
                        sesionUsuarioEntity.getDatosTemporalesJson(),
                        new TypeReference<List<AsientoVendido>>() {}
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SesionUsuario(
                sesionUsuarioEntity.getUsuarioId(),
                sesionUsuarioEntity.getEventoId(),
                sesionUsuarioEntity.getPasoActual(),
                asientos,
                sesionUsuarioEntity.getUltimaModificacion()
        );
    }
}