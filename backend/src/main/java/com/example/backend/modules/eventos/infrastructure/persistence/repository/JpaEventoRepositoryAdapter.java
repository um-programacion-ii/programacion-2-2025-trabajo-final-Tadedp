package com.example.backend.modules.eventos.infrastructure.persistence.repository;

import com.example.backend.modules.eventos.domain.model.Evento;
import com.example.backend.modules.eventos.domain.ports.out.EventoRepository;
import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import com.example.backend.modules.eventos.infrastructure.persistence.mapper.EventoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaEventoRepositoryAdapter implements EventoRepository {
    private final JpaEventoRepository jpaEventoRepository;
    private final EventoMapper eventoMapper;

    @Override
    public List<Evento> findAll() {
        return jpaEventoRepository.findAll().stream()
                .map(eventoMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Evento> findById(Long id) {
        return jpaEventoRepository.findById(id).map(eventoMapper::toDomain);
    }

    @Override
    public Evento save(Evento evento) {
        EventoEntity eventoEntity = eventoMapper.toEntity(evento);
        EventoEntity savedEvento = jpaEventoRepository.save(eventoEntity);
        return eventoMapper.toDomain(savedEvento);
    }

    @Override
    public void deleteById(Long id) {
        jpaEventoRepository.deleteById(id);
    }
}
