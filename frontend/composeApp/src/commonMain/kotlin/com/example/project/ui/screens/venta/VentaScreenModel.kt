package com.example.project.ui.screens.venta

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.project.data.remote.dto.AsientoPersonaDto
import com.example.project.data.remote.dto.SesionDto
import com.example.project.di.SesionExpiradaException
import com.example.project.domain.model.Asiento
import com.example.project.domain.repository.EventoRepository
import com.example.project.domain.repository.SesionRepository
import com.example.project.util.ManejadorSesion
import com.example.project.util.NetworkResult
import kotlinx.coroutines.launch

class VentaScreenModel(
    private val eventoRepository: EventoRepository,
    private val sesionRepository: SesionRepository,
    private val manejadorSesion: ManejadorSesion
) : ScreenModel {

    var estado by mutableStateOf<EstadoVenta>(EstadoVenta.Idle)
        private set

    fun procesarPago(eventoId: Long, asistentes: Map<Asiento, String>) {
        screenModelScope.launch {
            estado = EstadoVenta.Cargando

            val listaVenta = asistentes.map { (asiento, nombre) ->
                AsientoPersonaDto(
                    fila = asiento.fila,
                    columna = asiento.columna,
                    persona = nombre
                )
            }

            val ventaResultado = eventoRepository.comprarAsientos(eventoId, listaVenta)

            if (ventaResultado is NetworkResult.Error && ventaResultado.throwable is SesionExpiradaException) {
                manejadorSesion.logout()
            } else {
                estado = when (ventaResultado) {
                    is NetworkResult.Exito -> EstadoVenta.Exito
                    is NetworkResult.Error -> EstadoVenta.Error(ventaResultado.mensaje)
                    is NetworkResult.Cargando -> EstadoVenta.Cargando
                }
            }
        }
    }

    fun navegarAtras(eventoId: Long, asistentes: Map<Asiento, String>, onExito: () -> Unit) {
        screenModelScope.launch {
            val asientoDtos = asistentes.map { (asiento, nombre) ->
                AsientoPersonaDto(asiento.fila, asiento.columna, nombre)
            }

            sesionRepository.actualizarSesion(
                SesionDto(
                    eventoId = eventoId,
                    pasoActual = "CARGA_ASISTENTES",
                    asientosSeleccionados = asientoDtos
                )
            )
            onExito()
        }
    }
}

sealed class EstadoVenta {
    data object Idle : EstadoVenta()
    data object Cargando : EstadoVenta()
    data object Exito : EstadoVenta()
    data class Error(val mensaje: String) : EstadoVenta()
}