package com.example.project.domain.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.example.project.data.local.AuthSettings
import com.example.project.data.remote.dto.LoginRequest
import com.example.project.data.remote.dto.LoginResponse
import com.example.project.data.remote.dto.RegistroRequest
import com.example.project.util.NetworkResult
import com.example.project.util.RutasBackend

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val authSettings: AuthSettings
) : AuthRepository {

    override suspend fun login(username: String, password: String): NetworkResult<Boolean> {
        return try {
            val response = client.post(RutasBackend.LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }

            val loginResponse: LoginResponse = response.body()

            authSettings.guardarToken(loginResponse.token)

            NetworkResult.Exito(true)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("Error al iniciar sesión: ${e.message}", e)
        }
    }

    override suspend fun registro(request: RegistroRequest): NetworkResult<Boolean> {
        return try {
            val response = client.post(RutasBackend.REGISTRO) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            NetworkResult.Exito(true)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("Error en el registro: ${e.message}", e)
        }
    }

    override fun isUsuarioLoggedIn(): Boolean {
        return authSettings.obtenerToken() != null
    }

    override fun logout() {
        authSettings.removerToken()
    }
}