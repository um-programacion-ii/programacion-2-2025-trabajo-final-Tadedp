package com.example.backend.modules.eventos.domain.ports.out;

import com.example.backend.modules.eventos.domain.model.Evento;

import java.util.List;
import java.util.Optional;

public interface EventoRepository {
    List<Evento> findAll();
    Optional<Evento> findById(Long id);
    Evento save(Evento evento);
    Optional<Evento> update(Long id, Evento evento);
    void deleteById(Long id);
}
