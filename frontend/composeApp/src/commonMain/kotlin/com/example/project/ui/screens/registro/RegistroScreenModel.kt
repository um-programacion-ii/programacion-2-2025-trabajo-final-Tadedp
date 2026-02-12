package com.example.project.ui.screens.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.project.data.remote.dto.RegistroRequest
import com.example.project.domain.repository.AuthRepository
import com.example.project.util.NetworkResult
import kotlinx.coroutines.launch

class RegistroScreenModel(
    private val authRepository: AuthRepository
) : ScreenModel {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmarPassword by mutableStateOf("")
    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var email by mutableStateOf("")

    var estado by mutableStateOf<EstadoRegistro>(EstadoRegistro.Idle)
        private set

    fun onRegistroClickeado() {
        if (username.isBlank() || password.isBlank() || confirmarPassword.isBlank() || nombre.isBlank() || apellido.isBlank() || email.isBlank()) {
            estado = EstadoRegistro.Error("Todos los campos son obligatorios")
            return
        }

        if (password != confirmarPassword) {
            estado = EstadoRegistro.Error("Las contraseñas no coinciden")
            return
        }

        screenModelScope.launch {
            estado = EstadoRegistro.Cargando

            val registroRequest = RegistroRequest(username, password, nombre, apellido, email)
            val registroResultado = authRepository.registro(registroRequest)

            estado = when (registroResultado) {
                is NetworkResult.Exito -> EstadoRegistro.Exito
                is NetworkResult.Error -> EstadoRegistro.Error(registroResultado.mensaje)
                is NetworkResult.Cargando -> EstadoRegistro.Cargando
            }
        }
    }
}

sealed class EstadoRegistro {
    data object Idle : EstadoRegistro()
    data object Cargando : EstadoRegistro()
    data object Exito : EstadoRegistro()
    data class Error(val mensaje: String) : EstadoRegistro()
}