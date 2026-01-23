package com.example.project.domain.repository

import com.example.project.data.remote.dto.SesionDto
import com.example.project.util.NetworkResult

interface SesionRepository {
    suspend fun obtenerSesionActual(): NetworkResult<SesionDto>
    suspend fun actualizarSesion(sesion: SesionDto): NetworkResult<Unit>
    suspend fun cerrarSesion(): NetworkResult<Unit>
}