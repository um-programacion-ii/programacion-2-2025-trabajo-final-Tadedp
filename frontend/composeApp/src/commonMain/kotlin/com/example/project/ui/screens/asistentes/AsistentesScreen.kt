package com.example.project.ui.screens.asistentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.example.project.domain.model.Asiento
import com.example.project.ui.screens.venta.VentaScreen

@OptIn(ExperimentalMaterial3Api::class, InternalVoyagerApi::class)
class AsistentesScreen(
    val eventoId: Long,
    val asientosSeleccionados: List<Asiento>
) : Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<AsistentesScreenModel>()

        val onBack = {
            screenModel.navegarAtras(eventoId, asientosSeleccionados) {
                navigator.pop()
            }
        }

        BackHandler(enabled = true) {
            onBack()
        }

        LaunchedEffect(Unit) {
            screenModel.validar(asientosSeleccionados.size)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Datos de Asistentes") },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    "Ingrese el nombre completo del asistente correspondiente para cada entrada.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(asientosSeleccionados) { asiento ->
                        AsistenteItem(
                            asiento = asiento,
                            nombre = screenModel.obtenerNombre(asiento),
                            onCambioNombre = { nuevoNombre ->
                                screenModel.modificarNombre(asiento, nuevoNombre)
                                screenModel.validar(asientosSeleccionados.size)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        val asistentes = asientosSeleccionados.associateWith { screenModel.obtenerNombre(it) }
                        screenModel.cargarAsistentes(eventoId, asistentes) {
                            navigator.push(VentaScreen(eventoId, asistentes))
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = screenModel.isValido
                ) {
                    Text("IR AL PAGO")
                }
            }
        }
    }
}

@Composable
fun AsistenteItem(asiento: Asiento, nombre: String, onCambioNombre: (String) -> Unit) {
    OutlinedTextField(
        value = nombre,
        onValueChange = onCambioNombre,
        label = { Text("Asiento F${asiento.fila}-C${asiento.columna}") },
        placeholder = { Text("Nombre y Apellido") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}