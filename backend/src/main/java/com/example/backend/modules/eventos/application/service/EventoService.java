package com.example.backend.modules.eventos.application.service;

import com.example.backend.modules.compartido.domain.ports.out.CatedraIntegracion;
import com.example.backend.modules.eventos.domain.model.Evento;
import com.example.backend.modules.eventos.domain.ports.out.EventoRepository;
import com.example.backend.modules.eventos.domain.ports.out.ProxyIntegracion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;
    private final ProxyIntegracion proxyIntegracion;
    private final CatedraIntegracion catedraIntegracion;

    @Transactional(readOnly = true)
    public List<Evento> obtenerEventos() {
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evento obtenerDetalleEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento no encontrado"));

        return evento;
    }

    public Map<String, String> obtenerMapaAsientos(Long eventoId) {
        return proxyIntegracion.obtenerEstadoAsientos(eventoId);
    }

    @Transactional
    public void sincronizarEventos() {
        List<Evento> catedraEventos = catedraIntegracion.obtenerEventos();

        Set<Long> catedraIds = catedraEventos.stream()
                .map(Evento::getId)
                .collect(Collectors.toSet());

        for (Evento evento : catedraEventos) {
            eventoRepository.save(evento);
        }

        List<Evento> eventos = eventoRepository.findAll();

        for (Evento evento : eventos) {
            if (!catedraIds.contains(evento.getId())) {
                eventoRepository.deleteById(evento.getId());
            }
        }
    }
}
