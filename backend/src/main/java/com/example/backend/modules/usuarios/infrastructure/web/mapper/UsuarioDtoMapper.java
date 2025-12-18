package com.example.backend.modules.usuarios.infrastructure.web.mapper;

import com.example.backend.modules.usuarios.domain.model.Usuario;
import com.example.backend.modules.usuarios.infrastructure.web.dto.RegistroRequest;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDtoMapper {
    public Usuario toDomain(RegistroRequest registroRequest) {
        if  (registroRequest == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setPassword(registroRequest.getPassword());
        usuario.setNombre(registroRequest.getNombre());
        usuario.setApellido(registroRequest.getApellido());
        usuario.setEmail(registroRequest.getEmail());
        return usuario;
    }
}
