package com.example.project.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BloqueoRequest(
    val eventoId: Long,
    val asientosVendidos: List<AsientoSimpleDto>
)

@Serializable
data class VentaRequest(
    val eventoId: Long,
    val asientosVendidos: List<AsientoPersonaDto>
)

@Serializable
data class AsientoSimpleDto(
    val fila: Int,
    val columna: Int
)

@Serializable
data class AsientoPersonaDto(
    val fila: Int,
    val columna: Int,
    val persona: String? = null
)