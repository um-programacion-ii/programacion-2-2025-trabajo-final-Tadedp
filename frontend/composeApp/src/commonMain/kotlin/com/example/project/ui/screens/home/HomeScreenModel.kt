package com.example.project.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.project.data.remote.dto.EventoDto
import com.example.project.di.SesionExpiradaException
import com.example.project.domain.repository.EventoRepository
import com.example.project.util.ManejadorSesion
import com.example.project.util.NetworkResult
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val eventoRepository: EventoRepository,
    private val manejadorSesion: ManejadorSesion
) : ScreenModel {

    var estado by mutableStateOf<EstadoHome>(EstadoHome.Cargando)
        private set

    init {
        cargarEventos()
    }

    fun cargarEventos() {
        screenModelScope.launch {
            estado = EstadoHome.Cargando

            val resultado = eventoRepository.obtenerEventos()

            if (resultado is NetworkResult.Error && resultado.throwable is SesionExpiradaException) {
                manejadorSesion.logout()
            } else {
                estado = when (resultado) {
                    is NetworkResult.Exito -> {
                        if (resultado.data.isEmpty()) {
                            EstadoHome.Vacio
                        }
                        else {
                            EstadoHome.Exito(resultado.data)
                        }
                    }
                    is NetworkResult.Error -> EstadoHome.Error(resultado.mensaje)
                    is NetworkResult.Cargando -> EstadoHome.Cargando
                }
            }

        }
    }

    fun recargarEventos() {
        screenModelScope.launch {
            estado = EstadoHome.Cargando

            val resultado = eventoRepository.syncEventos()

            if (resultado is NetworkResult.Error) {
                if (resultado.throwable is SesionExpiradaException) {
                    manejadorSesion.logout()
                } else {
                    estado = EstadoHome.Error("Fallo al sincronizar: ${resultado.mensaje}")
                }
            } else {
                cargarEventos()
            }
        }
    }

    fun onLogoutClickeado() {
        manejadorSesion.logout()
    }
}

sealed class EstadoHome {
    data object Cargando : EstadoHome()
    data object Vacio : EstadoHome()
    data class Exito(val eventos: List<EventoDto>) : EstadoHome()
    data class Error(val mensaje: String) : EstadoHome()
}