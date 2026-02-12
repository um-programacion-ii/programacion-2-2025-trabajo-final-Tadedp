package com.example.project.data.local

import com.russhwolf.settings.Settings

class AuthSettings(private val settings: Settings) {

    companion object {
        private const val KEY_TOKEN = "auth_token"
    }

    fun guardarToken(token: String) {
        settings.putString(KEY_TOKEN, token)
    }

    fun obtenerToken(): String? {
        return settings.getStringOrNull(KEY_TOKEN)
    }

    fun removerToken() {
        settings.remove(KEY_TOKEN)
    }
}