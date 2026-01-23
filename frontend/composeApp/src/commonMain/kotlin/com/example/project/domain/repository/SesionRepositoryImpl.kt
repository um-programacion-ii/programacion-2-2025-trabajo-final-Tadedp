package com.example.project.domain.repository

import com.example.project.data.local.AuthSettings
import com.example.project.data.remote.dto.SesionDto
import com.example.project.util.NetworkResult
import com.example.project.util.RutasBackend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SesionRepositoryImpl(
    private val client: HttpClient,
    private val authSettings: AuthSettings
) : SesionRepository {

    override suspend fun obtenerSesionActual(): NetworkResult<SesionDto> {
        return try {
            val resultado = client.get(RutasBackend.SESION).body<SesionDto>()
            NetworkResult.Exito(resultado)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("Error al obtener sesión: ${e.message}", e)
        }
    }

    override suspend fun actualizarSesion(sesion: SesionDto): NetworkResult<Unit> {
        return try {
            client.put(RutasBackend.SESION) {
                contentType(ContentType.Application.Json)
                setBody(sesion)
            }
            NetworkResult.Exito(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("Error al actualizar sesión: ${e.message}", e)
        }
    }

    override suspend fun cerrarSesion(): NetworkResult<Unit> {
        return try {
            client.delete(RutasBackend.SESION)

            authSettings.removerToken()

            NetworkResult.Exito(Unit)
        } catch (e: Exception) {
            authSettings.removerToken()
            NetworkResult.Error("Error al cerrar sesión", e)
        }
    }
}