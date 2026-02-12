package com.example.backend.modules.usuarios.infrastructure.persistence.repository;

import com.example.backend.modules.usuarios.domain.model.Usuario;
import com.example.backend.modules.usuarios.domain.ports.out.UsuarioRepository;
import com.example.backend.modules.usuarios.infrastructure.persistence.entity.UsuarioEntity;
import com.example.backend.modules.usuarios.infrastructure.persistence.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return jpaUsuarioRepository.findByUsername(username).map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaUsuarioRepository.findById(id).map(usuarioMapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        return usuarioMapper.toDomain(jpaUsuarioRepository.save(usuarioEntity));
    }
}
