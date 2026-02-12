package com.example.project.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventoDto(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val imagen: String?,
    val precioEntrada: Double,
    val filaAsientos: Int,
    val columnAsientos: Int,
)

@Serializable
data class AsientoDto(
    val fila: Int,
    val columna: Int,
    val estado: String? = null,
    val persona: String? = null
)