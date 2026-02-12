package com.example.backend.modules.eventos.infrastructure.persistence.repository;


import com.example.backend.modules.eventos.infrastructure.persistence.entity.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventoRepository extends JpaRepository<EventoEntity, Long> {
}
