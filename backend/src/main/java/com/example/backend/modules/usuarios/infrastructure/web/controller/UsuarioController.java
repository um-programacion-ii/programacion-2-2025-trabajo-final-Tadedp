package com.example.backend.modules.usuarios.infrastructure.web.controller;

import com.example.backend.modules.compartido.infrastructure.security.JwtUtil;
import com.example.backend.modules.usuarios.application.service.UsuarioService;
import com.example.backend.modules.usuarios.domain.model.Usuario;
import com.example.backend.modules.usuarios.infrastructure.web.dto.LoginRequest;
import com.example.backend.modules.usuarios.infrastructure.web.dto.RegistroRequest;
import com.example.backend.modules.usuarios.infrastructure.web.mapper.UsuarioDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioDtoMapper  usuarioDtoMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = usuarioService.loginUsuario(request.getUsername(), request.getPassword());

            String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getId());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "usuarioId", usuario.getId(),
                    "username", usuario.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = usuarioDtoMapper.toDomain(request);

            Usuario usuarioRegistrado =  usuarioService.registrarUsuario(usuario);

            return ResponseEntity.ok(usuarioRegistrado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
