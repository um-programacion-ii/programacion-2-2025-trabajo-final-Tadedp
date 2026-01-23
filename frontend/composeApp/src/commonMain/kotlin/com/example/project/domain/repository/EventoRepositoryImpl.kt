package com.example.project.domain.repository

import com.example.project.data.remote.dto.AsientoPersonaDto
import com.example.project.data.remote.dto.AsientoSimpleDto
import com.example.project.data.remote.dto.BloqueoRequest
import com.example.project.data.remote.dto.EventoDto
import com.example.project.data.remote.dto.VentaRequest
import com.example.project.util.NetworkResult
import com.example.project.util.RutasBackend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class EventoRepositoryImpl(
    private val client: HttpClient
) : EventoRepository {

    override suspend fun obtenerEventos(): NetworkResult<List<EventoDto>> {
        return llamadaSegura {
            client.get(RutasBackend.EVENTOS).body()
        }
    }

    override suspend fun obtenerDetalleEvento(id: Long): NetworkResult<EventoDto> {
        return llamadaSegura {
            client.get("${RutasBackend.EVENTOS}/$id").body()
        }
    }

    override suspend fun syncEventos(): NetworkResult<Unit> {
        return llamadaSegura {
            client.post(RutasBackend.SYNC)
        }
    }

    override suspend fun obtenerAsientos(eventoId: Long): NetworkResult<Map<String, String>> {
        return llamadaSegura {
            client.get("${RutasBackend.EVENTOS}/$eventoId/asientos").body()
        }
    }

    override suspend fun bloquearAsientos(eventoId: Long, asientos: List<AsientoSimpleDto>): NetworkResult<Unit> {
        return llamadaSegura {
            val request = BloqueoRequest(eventoId, asientos)
            client.post(RutasBackend.BLOQUEAR) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun comprarAsientos(eventoId: Long, asientos: List<AsientoPersonaDto>): NetworkResult<Unit> {
        return llamadaSegura {
            val request = VentaRequest(eventoId, asientos)
            client.post(RutasBackend.COMPRAR) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    private suspend fun <T> llamadaSegura(llamada: suspend () -> T): NetworkResult<T> {
        return try {
            val resultado = llamada()
            NetworkResult.Exito(resultado)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("Error de comunicación: ${e.message}", e)
        }
    }
}