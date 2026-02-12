package com.example.project.util

import com.example.project.domain.repository.SesionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ManejadorSesion(
    private val sesionRepository: SesionRepository
) {
    private val _eventoLogout = MutableSharedFlow<Unit>()
    val eventoLogout = _eventoLogout.asSharedFlow()

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            sesionRepository.cerrarSesion()
            _eventoLogout.emit(Unit)
        }
    }
}