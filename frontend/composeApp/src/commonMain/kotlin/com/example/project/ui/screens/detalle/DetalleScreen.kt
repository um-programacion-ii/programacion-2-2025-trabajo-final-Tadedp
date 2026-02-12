package com.example.project.ui.screens.detalle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.AsyncImage
import com.example.project.ui.components.MapaAsientos
import com.example.project.ui.screens.asistentes.AsistentesScreen

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
data class DetalleScreen (val eventoId: Long) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<DetalleScreenModel>()
        val estado = screenModel.estado

        val onBack = {
            screenModel.navegarAtras {
                navigator.pop()
            }
        }

        BackHandler(enabled = true) {
            onBack()
        }

        LaunchedEffect(eventoId) {
            screenModel.cargarDataEvento(eventoId)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Selección de Asientos") },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (val resultado = estado) {
                    is EstadoDetalle.Cargando -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is EstadoDetalle.Error -> {
                        Text("Error: ${resultado.mensaje}", modifier = Modifier.align(Alignment.Center))
                    }
                    is EstadoDetalle.Exito -> {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = resultado.evento.imagen,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(end = 16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Column {
                                    Text(resultado.evento.titulo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Fecha: ${resultado.evento.fecha.take(10)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Precio: $ ${resultado.evento.precioEntrada}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Descripción:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(
                                text = resultado.evento.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Selecciona tus asientos (Máx 4):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                MapaAsientos(
                                    asientos = resultado.asientos,
                                    columnas = resultado.evento.columnAsientos,
                                    asientosSeleccionados = screenModel.asientosSeleccionados,
                                    onAsientoClick = { screenModel.cambiarSeleccionAsiento(it) }
                                )
                            }

                            Button(
                                onClick = {
                                    screenModel.confirmarSeleccion {
                                        navigator.push(AsistentesScreen(eventoId, screenModel.asientosSeleccionados))
                                        println("Asientos bloqueados exitosamente. Avanzando...")
                                    }
                                },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    enabled = screenModel.asientosSeleccionados.isNotEmpty()
                            ) {
                                Text("CONTINUAR (${screenModel.asientosSeleccionados.size})")
                            }
                        }
                    }
                }
            }
        }
    }
}