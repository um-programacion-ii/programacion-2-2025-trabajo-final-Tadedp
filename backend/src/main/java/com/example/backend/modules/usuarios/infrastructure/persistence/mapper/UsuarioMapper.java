package com.example.backend.modules.usuarios.infrastructure.persistence.mapper;

import com.example.backend.modules.usuarios.domain.model.Usuario;
import com.example.backend.modules.usuarios.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(usuario.getId());
        usuarioEntity.setUsername(usuario.getUsername());
        usuarioEntity.setPassword(usuario.getPassword());
        usuarioEntity.setNombre(usuario.getNombre());
        usuarioEntity.setApellido(usuario.getApellido());
        usuarioEntity.setEmail(usuario.getEmail());
        usuarioEntity.setActivo(usuario.isActivo());
        return usuarioEntity;
    }

    public Usuario toDomain(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }

        return new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getUsername(),
                usuarioEntity.getPassword(),
                usuarioEntity.getNombre(),
                usuarioEntity.getApellido(),
                usuarioEntity.getEmail(),
                usuarioEntity.isActivo()
        );
    }
}
