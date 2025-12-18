package com.example.backend.modules.ventas.infrastructure.web.controller;

import com.example.backend.modules.compartido.infrastructure.security.CustomUserDetails;
import com.example.backend.modules.ventas.application.service.SesionUsuarioService;
import com.example.backend.modules.ventas.domain.model.SesionUsuario;
import com.example.backend.modules.ventas.infrastructure.web.dto.SesionUsuarioRequest;
import com.example.backend.modules.ventas.infrastructure.web.mapper.SesionUsuarioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sesion")
@RequiredArgsConstructor
public class SesionUsuarioController {
    private final SesionUsuarioService sesionUsuarioService;
    private final SesionUsuarioDtoMapper sesionUsuarioDtoMapper;

    @GetMapping
    public ResponseEntity<SesionUsuario> obtenerEstadoSesion(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(sesionUsuarioService.obtenerSesion(userDetails.getId()));
    }

    @PutMapping
    public ResponseEntity<Void> actualizarEstadoSesion(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SesionUsuarioRequest request) {
        SesionUsuario sesionUsuario = sesionUsuarioDtoMapper.toDomain(request);

        sesionUsuarioService.actualizarEstadoSesion(
                userDetails.getId(),
                sesionUsuario.getEventoId(),
                sesionUsuario.getPasoActual(),
                sesionUsuario.getAsientosSeleccionados()
        );

        return ResponseEntity.ok().build();
    }
}
