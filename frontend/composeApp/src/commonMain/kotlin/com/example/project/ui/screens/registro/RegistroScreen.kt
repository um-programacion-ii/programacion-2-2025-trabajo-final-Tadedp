package com.example.project.ui.screens.registro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.project.ui.screens.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
object RegistroScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<RegistroScreenModel>()
        val estado = screenModel.estado

        LaunchedEffect(estado) {
            if (estado is EstadoRegistro.Exito) {
                navigator.replaceAll(LoginScreen)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear Cuenta") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = screenModel.username,
                    onValueChange = { screenModel.username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = screenModel.password,
                    onValueChange = { screenModel.password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = screenModel.confirmarPassword,
                    onValueChange = { screenModel.confirmarPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    isError = screenModel.password.isNotEmpty() &&
                            screenModel.confirmarPassword.isNotEmpty() &&
                            screenModel.password != screenModel.confirmarPassword
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = screenModel.nombre,
                    onValueChange = { screenModel.nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = screenModel.apellido,
                    onValueChange = { screenModel.apellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = screenModel.email,
                    onValueChange = { screenModel.email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (estado is EstadoRegistro.Error) {
                    Text(
                        text = estado.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                if (estado is EstadoRegistro.Cargando) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { screenModel.onRegistroClickeado() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = screenModel.username.isNotBlank() && screenModel.password.isNotBlank() && screenModel.nombre.isNotBlank() && screenModel.apellido.isNotBlank() && screenModel.email.isNotBlank()
                    ) {
                        Text("REGISTRARSE")
                    }
                }
            }
        }
    }
}