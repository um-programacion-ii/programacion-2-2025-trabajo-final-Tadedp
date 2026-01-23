package com.example.project.ui.screens.venta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.example.project.domain.model.Asiento
import com.example.project.ui.screens.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
data class VentaScreen(
    val eventoId: Long,
    val asistentes: Map<Asiento, String>
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<VentaScreenModel>()
        val estado = screenModel.estado

        val precioTotal = asistentes.keys.sumOf { it.precio }

        val onBack = {
            screenModel.navegarAtras(eventoId, asistentes) {
                navigator.pop()
            }
        }

        if (estado !is EstadoVenta.Exito) {
            BackHandler(enabled = true) {
                onBack()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirmar Compra") },
                    navigationIcon = {
                        if (estado !is EstadoVenta.Exito) {
                            IconButton(onClick = { onBack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Resumen del Pedido", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            asistentes.forEach { (asiento, nombre) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Fila ${asiento.fila} - Col ${asiento.columna}", fontWeight = FontWeight.Bold)
                                        Text(nombre, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text("$ ${asiento.precio}")
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("TOTAL", fontWeight = FontWeight.Black, fontSize = 18.sp)
                                Text("$ $precioTotal", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    when (estado) {
                        is EstadoVenta.Cargando -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(enabled = false) {},
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator()
                                    Text("Procesando pago...", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                }
                            }
                        }

                        is EstadoVenta.Error -> {
                            Text(
                                text = estado.mensaje,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(
                                onClick = { screenModel.procesarPago(eventoId, asistentes) },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("REINTENTAR")
                            }
                            Button(
                                onClick = { navigator.replaceAll(HomeScreen) },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("VOLVER AL INICIO")
                            }
                        }

                        is EstadoVenta.Exito -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Éxito",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(64.dp)
                            )
                            Text("¡Compra Exitosa!", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4CAF50))
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navigator.replaceAll(HomeScreen) },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("VOLVER AL INICIO")
                            }
                        }

                        is EstadoVenta.Idle -> {
                            Button(
                                onClick = { screenModel.procesarPago(eventoId, asistentes) },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text("CONFIRMAR Y COMPRAR")
                            }
                        }
                    }
                }
            }
        }
    }
}