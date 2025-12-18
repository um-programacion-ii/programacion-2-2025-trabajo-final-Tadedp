package com.example.backend.modules.eventos.infrastructure.persistence.mapper;

import com.example.backend.modules.eventos.domain.model.Evento;
import com.example.backend.modules.eventos.domain.model.Integrante;
import com.example.backend.modules.eventos.domain.model.TipoEvento;
import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import com.example.backend.modules.eventos.infrastructure.persistence.entity.IntegranteEntity;
import com.example.backend.modules.eventos.infrastructure.persistence.entity.TipoEventoEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventoMapper {
    public EventoEntity toEntity(Evento evento) {
        if (evento == null) {
            return null;
        }

        EventoEntity eventoEntity = new EventoEntity();
        eventoEntity.setId(evento.getId());
        eventoEntity.setTitulo(evento.getTitulo());
        eventoEntity.setResumen(evento.getResumen());
        eventoEntity.setDescripcion(evento.getDescripcion());
        eventoEntity.setFecha(evento.getFecha());
        eventoEntity.setDireccion(evento.getDireccion());
        eventoEntity.setImagen(evento.getImagen());
        eventoEntity.setFilaAsientos(evento.getFilaAsientos());
        eventoEntity.setColumnAsientos(evento.getColumnAsientos());
        eventoEntity.setPrecioEntrada(evento.getPrecioEntrada());

        TipoEventoEntity tipoEventoEntity = new TipoEventoEntity(
                evento.getEventoTipo().getId(),
                evento.getEventoTipo().getNombre(),
                evento.getEventoTipo().getDescripcion()
        );
        eventoEntity.setEventoTipo(tipoEventoEntity);

        Set<IntegranteEntity> integrantesEntities = evento.getIntegrantes().stream()
                .map(integrante -> {
                    IntegranteEntity integranteEntity = new IntegranteEntity(
                            integrante.getId(),
                            integrante.getNombre(),
                            integrante.getApellido(),
                            integrante.getIdentificacion()
                    );
                    return integranteEntity;
                }).collect(Collectors.toSet());
        eventoEntity.setIntegrantes(integrantesEntities);

        return eventoEntity;
    }

    public Evento toDomain(EventoEntity eventoEntity) {
        if (eventoEntity == null) {
            return null;
        }

        TipoEvento tipoEvento = new TipoEvento(
                eventoEntity.getEventoTipo().getId(),
                eventoEntity.getEventoTipo().getNombre(),
                eventoEntity.getEventoTipo().getDescripcion()
        );

        Set<Integrante> integrantes = eventoEntity.getIntegrantes().stream()
                .map(integranteEntity -> {
                    Integrante integrante = new Integrante(
                            integranteEntity.getId(),
                            integranteEntity.getNombre(),
                            integranteEntity.getApellido(),
                            integranteEntity.getIdentificacion()
                    );
                    return integrante;
                }).collect(Collectors.toSet());

        return new Evento(
                eventoEntity.getId(),
                eventoEntity.getTitulo(),
                eventoEntity.getResumen(),
                eventoEntity.getDescripcion(),
                eventoEntity.getFecha(),
                eventoEntity.getDireccion(),
                eventoEntity.getImagen(),
                eventoEntity.getFilaAsientos(),
                eventoEntity.getColumnAsientos(),
                eventoEntity.getPrecioEntrada(),
                tipoEvento,
                integrantes
        );
    }
}
