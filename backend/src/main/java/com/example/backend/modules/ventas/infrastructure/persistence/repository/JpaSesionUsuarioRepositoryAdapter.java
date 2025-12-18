package com.example.backend.modules.ventas.infrastructure.persistence.repository;

import com.example.backend.modules.ventas.domain.model.SesionUsuario;
import com.example.backend.modules.ventas.domain.ports.out.SesionUsuarioRepository;
import com.example.backend.modules.ventas.infrastructure.persistence.entity.SesionUsuarioEntity;
import com.example.backend.modules.ventas.infrastructure.persistence.mapper.SesionUsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaSesionUsuarioRepositoryAdapter implements SesionUsuarioRepository {
    private final JpaSesionUsuarioRepository jpaSesionUsuarioRepository;
    private final SesionUsuarioMapper sesionUsuarioMapper;

    @Override
    public Optional<SesionUsuario> findByUsuarioId(Long usuarioId) {
        return jpaSesionUsuarioRepository.findById(usuarioId).map(sesionUsuarioMapper::toDomain);
    }

    @Override
    public SesionUsuario save(SesionUsuario sesionUsuario) {
        SesionUsuarioEntity sesionUsuarioEntity = sesionUsuarioMapper.toEntity(sesionUsuario);
        return sesionUsuarioMapper.toDomain(jpaSesionUsuarioRepository.save(sesionUsuarioEntity));
    }

    @Override
    public void deleteByUsuarioId(Long usuarioId) {
        if (jpaSesionUsuarioRepository.existsById(usuarioId)) {
            jpaSesionUsuarioRepository.deleteById(usuarioId);
        }
    }
}
