package com.example.project.domain.model

data class Asiento(
    val fila: Int,
    val columna: Int,
    val estado: EstadoAsiento,
    val precio: Double
) {
    val id: String get() = "$fila-$columna"
}

enum class EstadoAsiento {
    DISPONIBLE,
    SELECCIONADO,
    VENDIDO,
    BLOQUEADO
}