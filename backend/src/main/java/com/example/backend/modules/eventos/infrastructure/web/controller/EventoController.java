package com.example.backend.modules.eventos.infrastructure.web.controller;

import com.example.backend.modules.eventos.application.service.EventoService;
import com.example.backend.modules.eventos.domain.model.Evento;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {
    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.obtenerEventos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> detalleEvento(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.obtenerDetalleEvento(id));
    }

    @GetMapping("/{id}/asientos")
    public ResponseEntity<Map<String, String>> verAsientos(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.obtenerMapaAsientos(id));
    }

    @PostMapping("/sync")
    public ResponseEntity<String> forzarSincronizacion() {
        eventoService.sincronizarEventos();
        return ResponseEntity.ok("Sincronización iniciada");
    }
}