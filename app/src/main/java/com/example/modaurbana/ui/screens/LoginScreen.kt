package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthUiState
import com.example.modaurbana.viewmodel.AuthViewModel

/**
 * Formulario con validaciones (requeridos, formato), bloquea envío inválido,
 * muestra mensajes por campo (Guía 11) y estados de carga/éxito/error (Guía 12).
 * COMPLETA las validaciones en el ViewModel y vincula isError/supportingText aquí.
 */
@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = viewModel()) {
    val ui: AuthUiState by vm.ui.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // TODO: derivedStateOf para habilitar botón solo si campos válidos (Guía 12)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = { username = it },
            label = { Text("Usuario") } /* TODO: isError + supportingText con mensaje específico (Guía 11) */)

        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation()
            /* TODO: isError + supportingText */)

        if (ui.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(ui.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { vm.login(username, password) },
            enabled = !ui.isLoading /* && campos válidos */
        ) {
            if (ui.isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp))
            else Text("Entrar")
        }

        TextButton(onClick = { navController.navigate(Route.Register.path) }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        if (ui.success) {
            // Navega a Perfil y limpia backstack (Guía 10 + EP3 back correcto)
            LaunchedEffect(true) {
                navController.navigate(Route.Profile.path) {
                    popUpTo(Route.Login.path) { inclusive = true }
                }
            }
        }
    }
}
