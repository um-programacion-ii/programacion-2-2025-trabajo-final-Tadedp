package com.example.project.util

sealed class NetworkResult<out T> {
    data class Exito<out T>(val data: T) : NetworkResult<T>()
    data class Error(val mensaje: String, val throwable: Throwable? = null) : NetworkResult<Nothing>()
    data object Cargando : NetworkResult<Nothing>()
}