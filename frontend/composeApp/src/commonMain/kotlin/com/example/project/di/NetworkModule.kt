package com.example.project.di

import com.example.project.data.local.AuthSettings
import com.example.project.util.Constantes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        val authSettings: AuthSettings = get()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR_LOG: $message")
                    }
                }
            }

            install(DefaultRequest) {
                url(Constantes.BACKEND_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)

                authSettings.obtenerToken()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            HttpResponseValidator {
                validateResponse { respuesta ->
                    val estado = respuesta.status.value
                    if (estado == 403) {
                        authSettings.removerToken()
                        throw SesionExpiradaException()
                    }
                }
            }
        }
    }
}

class SesionExpiradaException : Exception("La sesión ha expirado")