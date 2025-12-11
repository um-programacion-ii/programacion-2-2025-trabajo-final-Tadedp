package com.example.backend.modules.usuarios.domain.ports.out;

import com.example.backend.modules.usuarios.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    Usuario save(Usuario usuario);
}
