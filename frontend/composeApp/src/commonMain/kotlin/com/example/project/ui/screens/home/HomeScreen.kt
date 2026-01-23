package com.example.project.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.project.ui.components.EventoCard
import com.example.project.ui.screens.detalle.DetalleScreen

@OptIn(ExperimentalMaterial3Api::class)
object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<HomeScreenModel>()
        val estado = screenModel.estado

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Próximos Eventos") },
                    actions = {
                        IconButton(onClick = { screenModel.recargarEventos() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Sincronizar")
                        }
                        IconButton(onClick = { screenModel.onLogoutClickeado() }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                when (estado) {
                    is EstadoHome.Cargando -> {
                        CircularProgressIndicator()
                    }

                    is EstadoHome.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = estado.mensaje,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(onClick = { screenModel.cargarEventos() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    is EstadoHome.Vacio -> {
                        Text("No hay eventos disponibles por ahora.")
                    }

                    is EstadoHome.Exito -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(estado.eventos) { evento ->
                                EventoCard(
                                    evento = evento,
                                    onClick = {
                                        navigator.push(DetalleScreen(evento.id))
                                        println("Click en evento: ${evento.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}