package com.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.project.ui.screens.login.LoginScreen
import com.example.project.util.ManejadorSesion
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        val manejadorSesion = koinInject<ManejadorSesion>()

        Navigator(LoginScreen) { navigator ->
            LaunchedEffect(Unit) {
                manejadorSesion.eventoLogout.collect {
                    navigator.replaceAll(LoginScreen)
                }
            }

            SlideTransition(navigator)
        }
    }
}