package com.example.project.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SesionDto(
    val usuarioId: Long? = null,
    val eventoId: Long? = null,
    val pasoActual: String? = null,
    val asientosSeleccionados: List<AsientoPersonaDto>? = null,
    val ultimaModificacion: String? = null
)