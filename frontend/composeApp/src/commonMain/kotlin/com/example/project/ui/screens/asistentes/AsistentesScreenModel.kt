package com.example.project.ui.screens.asistentes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.project.data.remote.dto.AsientoPersonaDto
import com.example.project.data.remote.dto.SesionDto
import com.example.project.domain.model.Asiento
import com.example.project.domain.repository.SesionRepository
import kotlinx.coroutines.launch

class AsistentesScreenModel(
    private val sesionRepository: SesionRepository
) : ScreenModel {
    private val asistentes = mutableStateMapOf<String, String>()

    var isValido by mutableStateOf(false)
        private set

    fun modificarNombre(asiento: Asiento, nombre: String) {
        val key = "${asiento.fila}-${asiento.columna}"
        asistentes[key] = nombre
        validar(asistentes.size)
    }

    fun obtenerNombre(asiento: Asiento): String {
        return asistentes["${asiento.fila}-${asiento.columna}"] ?: ""
    }

    fun validar(totalAsientos: Int) {
        val todosValidos = asistentes.size == totalAsientos && asistentes.values.all { it.isNotBlank() }
        isValido = todosValidos
    }

    fun cargarAsistentes(eventoId: Long, asistentes: Map<Asiento, String>, onExito: () -> Unit) {
        screenModelScope.launch {
            val asientoDtos = asistentes.map { (asiento, nombre) -> AsientoPersonaDto(asiento.fila, asiento.columna, nombre) }

            sesionRepository.actualizarSesion(
                SesionDto(
                    eventoId = eventoId,
                    pasoActual = "PAGO",
                    asientosSeleccionados = asientoDtos
                )
            )
            onExito()
        }
    }

    fun navegarAtras(eventoId: Long, asientosSeleccionados: List<Asiento>, onExito: () -> Unit) {
        screenModelScope.launch {
            val asientoDtos = asientosSeleccionados.map { AsientoPersonaDto(it.fila, it.columna, "") }

            sesionRepository.actualizarSesion(
                SesionDto(
                    eventoId = eventoId,
                    pasoActual = "SELECCION_ASIENTOS",
                    asientosSeleccionados = asientoDtos
                )
            )
            onExito()
        }
    }
}