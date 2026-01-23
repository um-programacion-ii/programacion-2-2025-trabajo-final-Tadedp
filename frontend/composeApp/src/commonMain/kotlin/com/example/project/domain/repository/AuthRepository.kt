package com.example.project.domain.repository

import com.example.project.data.remote.dto.RegistroRequest
import com.example.project.util.NetworkResult

interface AuthRepository {
    suspend fun login(username: String, password: String): NetworkResult<Boolean>
    suspend fun registro(request: RegistroRequest): NetworkResult<Boolean>
    fun isUsuarioLoggedIn(): Boolean
    fun logout()
}