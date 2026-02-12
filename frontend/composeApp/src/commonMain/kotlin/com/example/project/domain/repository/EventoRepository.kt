package com.example.project.domain.repository

import com.example.project.data.remote.dto.AsientoPersonaDto
import com.example.project.data.remote.dto.AsientoSimpleDto
import com.example.project.data.remote.dto.EventoDto
import com.example.project.util.NetworkResult

interface EventoRepository {
    suspend fun obtenerEventos(): NetworkResult<List<EventoDto>>
    suspend fun obtenerDetalleEvento(id: Long): NetworkResult<EventoDto>
    suspend fun syncEventos(): NetworkResult<Unit>

    suspend fun obtenerAsientos(eventoId: Long): NetworkResult<Map<String, String>>
    suspend fun bloquearAsientos(eventoId: Long, asientos: List<AsientoSimpleDto>): NetworkResult<Unit>
    suspend fun comprarAsientos(eventoId: Long, asientos: List<AsientoPersonaDto>): NetworkResult<Unit>
}