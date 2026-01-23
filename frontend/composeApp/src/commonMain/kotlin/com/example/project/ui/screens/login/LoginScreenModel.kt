package com.example.project.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.example.project.domain.model.Asiento
import com.example.project.domain.model.EstadoAsiento
import com.example.project.domain.repository.AuthRepository
import com.example.project.domain.repository.EventoRepository
import com.example.project.domain.repository.SesionRepository
import com.example.project.ui.screens.asistentes.AsistentesScreen
import com.example.project.ui.screens.detalle.DetalleScreen
import com.example.project.ui.screens.home.HomeScreen
import com.example.project.ui.screens.venta.VentaScreen
import com.example.project.util.NetworkResult
import kotlinx.coroutines.launch

class LoginScreenModel (
    private val authRepository: AuthRepository,
    private val sesionRepository: SesionRepository,
    private val eventoRepository: EventoRepository
) : ScreenModel {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var estado by mutableStateOf<EstadoLogin>(EstadoLogin.Idle)
        private set

    fun onLoginClickeado() {
        if (username.isBlank() || password.isBlank()) {
            estado = EstadoLogin.Error("Los campos son requeridos.")
            return
        }

        screenModelScope.launch {
            estado = EstadoLogin.Cargando

            val resultado = authRepository.login(username, password)

            if (resultado is NetworkResult.Exito) {
                chequearSesionYRedirigir()
            } else {
                estado = when (resultado) {
                    is NetworkResult.Error -> EstadoLogin.Error(resultado.mensaje)
                    is NetworkResult.Cargando -> EstadoLogin.Cargando
                }
            }
        }
    }

    private suspend fun chequearSesionYRedirigir() {
        val resultadoSesion = sesionRepository.obtenerSesionActual()

        if (resultadoSesion is NetworkResult.Exito) {
            val sesion = resultadoSesion.data
            val eventoId = sesion.eventoId

            val pantallasSesion = mutableListOf<Screen>(HomeScreen)

            if (eventoId != null) {
                pantallasSesion.add(DetalleScreen(eventoId))

                when (sesion.pasoActual) {
                    "CARGA_ASISTENTES", "PAGO" -> {
                        val detallesEvento = eventoRepository.obtenerDetalleEvento(eventoId)

                        if (detallesEvento is NetworkResult.Exito && !sesion.asientosSeleccionados.isNullOrEmpty()) {
                            try {
                                val asientoDtos = sesion.asientosSeleccionados
                                val precio = detallesEvento.data.precioEntrada

                                val asientosPreseleccionados = asientoDtos.map { dto ->
                                    Asiento(dto.fila, dto.columna, EstadoAsiento.SELECCIONADO, precio)
                                }

                                pantallasSesion.add(AsistentesScreen(eventoId, asientosPreseleccionados))

                                if (sesion.pasoActual == "PAGO") {
                                    val asistentesMap = asientosPreseleccionados.zip(asientoDtos).associate { (seat, dto) ->
                                        seat to (dto.persona ?: "")
                                    }
                                    pantallasSesion.add(VentaScreen(eventoId, asistentesMap))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            estado = EstadoLogin.Exito(pantallasSesion)
        } else {
            estado = EstadoLogin.Exito(listOf(HomeScreen))
        }
    }
}

sealed class EstadoLogin {
    data object Idle : EstadoLogin()
    data object Cargando : EstadoLogin()
    data class Exito(val pantallas: List<Screen>) : EstadoLogin()
    data class Error(val mensaje: String) : EstadoLogin()
}