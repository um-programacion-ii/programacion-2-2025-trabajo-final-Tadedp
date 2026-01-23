package com.example.project.ui.screens.detalle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.project.data.remote.dto.AsientoPersonaDto
import com.example.project.data.remote.dto.AsientoSimpleDto
import com.example.project.data.remote.dto.EventoDto
import com.example.project.data.remote.dto.SesionDto
import com.example.project.di.SesionExpiradaException
import com.example.project.domain.model.Asiento
import com.example.project.domain.model.EstadoAsiento
import com.example.project.domain.repository.EventoRepository
import com.example.project.domain.repository.SesionRepository
import com.example.project.util.ManejadorSesion
import com.example.project.util.NetworkResult
import kotlinx.coroutines.launch

class DetalleScreenModel(
    private val eventoRepository: EventoRepository,
    private val sesionRepository: SesionRepository,
    private val manejadorSesion: ManejadorSesion
) : ScreenModel {

    var estado by mutableStateOf<EstadoDetalle>(EstadoDetalle.Cargando)
        private set

    var asientosSeleccionados by mutableStateOf<List<Asiento>>(emptyList())
        private set

    private var evento: EventoDto? = null

    fun cargarDataEvento(eventoId: Long) {
        screenModelScope.launch {
            estado = EstadoDetalle.Cargando

            val eventoResultado = eventoRepository.obtenerDetalleEvento(eventoId)

            if (eventoResultado is NetworkResult.Error && eventoResultado.throwable is SesionExpiradaException) {
                manejadorSesion.logout()
            } else {
                val asientosResultado = eventoRepository.obtenerAsientos(eventoId)

                if (asientosResultado is NetworkResult.Error && asientosResultado.throwable is SesionExpiradaException) {
                    manejadorSesion.logout()
                } else {
                    val sesionResultado = sesionRepository.obtenerSesionActual()
                    val asientosPreseleccionados = mutableSetOf<String>()

                    if (sesionResultado is NetworkResult.Exito) {
                        val sesion = sesionResultado.data

                        if (sesion.eventoId == eventoId && !sesion.asientosSeleccionados.isNullOrEmpty()) {
                            try {
                                sesion.asientosSeleccionados.forEach {
                                    asientosPreseleccionados.add("${it.fila}-${it.columna}")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    if (eventoResultado is NetworkResult.Exito && asientosResultado is NetworkResult.Exito) {
                        evento = eventoResultado.data
                        val asientosOcupados = asientosResultado.data

                        val asientos = mutableListOf<Asiento>()
                        val preseleccion = mutableListOf<Asiento>()
                        for (fila in 1..eventoResultado.data.filaAsientos) {
                            for (columna in 1..eventoResultado.data.columnAsientos) {
                                val key = "$fila-$columna"
                                val estadoString = asientosOcupados[key]

                                val isPreseleccionado = asientosPreseleccionados.contains(key)

                                val estadoAsiento = when {
                                    isPreseleccionado -> EstadoAsiento.SELECCIONADO
                                    estadoString == "Vendido" -> EstadoAsiento.VENDIDO
                                    estadoString == "Bloqueado" -> EstadoAsiento.BLOQUEADO
                                    else -> EstadoAsiento.DISPONIBLE
                                }

                                val asiento = Asiento(
                                    fila = fila,
                                    columna = columna,
                                    estado = estadoAsiento,
                                    precio = eventoResultado.data.precioEntrada
                                )
                                asientos.add(asiento)

                                if (estadoAsiento == EstadoAsiento.SELECCIONADO) {
                                    preseleccion.add(asiento)
                                }
                            }
                        }

                        asientosSeleccionados = preseleccion

                        sesionRepository.actualizarSesion(
                            SesionDto(
                                eventoId = eventoId,
                                pasoActual = "SELECCION_ASIENTOS",
                                asientosSeleccionados = emptyList()
                            )
                        )

                        estado = EstadoDetalle.Exito(eventoResultado.data, asientos)
                    } else {
                        estado = EstadoDetalle.Error("Error al cargar datos del evento")
                    }
                }
            }
        }
    }

    fun cambiarSeleccionAsiento(asiento: Asiento) {
        if (asiento.estado == EstadoAsiento.VENDIDO || asiento.estado == EstadoAsiento.BLOQUEADO) return

        val isSeleccionado = asientosSeleccionados.any { it.id == asiento.id }

        if (isSeleccionado) {
            asientosSeleccionados = asientosSeleccionados.filterNot { it.id == asiento.id }
            refrescarMapaAsientos(asiento.id, EstadoAsiento.DISPONIBLE)
        } else {
            if (asientosSeleccionados.size < 4) {
                val nuevoAsiento = asiento.copy(estado = EstadoAsiento.SELECCIONADO)
                asientosSeleccionados = asientosSeleccionados + nuevoAsiento
                refrescarMapaAsientos(nuevoAsiento.id, EstadoAsiento.SELECCIONADO)
            }
        }
    }

    private fun refrescarMapaAsientos(asientoId: String, nuevoEstado: EstadoAsiento) {
        val estadoActual = estado
        if (estadoActual is EstadoDetalle.Exito) {
            val asientosRefrescados = estadoActual.asientos.map {
                if (it.id == asientoId) it.copy(estado = nuevoEstado) else it
            }
            estado = estadoActual.copy(asientos = asientosRefrescados)
        }
    }

    fun confirmarSeleccion(onExito: () -> Unit) {
        val evento = evento ?: return
        if (asientosSeleccionados.isEmpty()) return

        screenModelScope.launch {
            val asientos = asientosSeleccionados.map { AsientoSimpleDto(it.fila, it.columna) }
            val asientoDtos = asientos.map {
                AsientoPersonaDto(it.fila, it.columna, "")
            }

            val bloqueoResultado = eventoRepository.bloquearAsientos(evento.id, asientos)

            if (bloqueoResultado is NetworkResult.Error && bloqueoResultado.throwable is SesionExpiradaException) {
                manejadorSesion.logout()
            } else {
                if (bloqueoResultado is NetworkResult.Exito) {
                    sesionRepository.actualizarSesion(
                        SesionDto(
                            eventoId = evento.id,
                            pasoActual = "CARGA_ASISTENTES",
                            asientosSeleccionados = asientoDtos
                        )
                    )
                    onExito()
                } else {
                    cargarDataEvento(evento.id)
                }
            }
        }
    }

    fun navegarAtras(onExito: () -> Unit) {
        screenModelScope.launch {
            sesionRepository.actualizarSesion(
                SesionDto(eventoId = null, pasoActual = "INICIO", asientosSeleccionados = emptyList())
            )
            onExito()
        }
    }
}

sealed class EstadoDetalle {
    data object Cargando : EstadoDetalle()
    data class Exito(val evento: EventoDto, val asientos: List<Asiento>) : EstadoDetalle()
    data class Error(val mensaje: String) : EstadoDetalle()

}