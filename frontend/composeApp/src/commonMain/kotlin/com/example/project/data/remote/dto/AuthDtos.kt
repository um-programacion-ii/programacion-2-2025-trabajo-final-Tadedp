package com.example.project.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class RegistroRequest(
    val username: String,
    val password: String,
    val nombre: String,
    val apellido: String,
    val email: String
)