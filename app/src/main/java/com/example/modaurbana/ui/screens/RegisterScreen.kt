package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel
import com.example.modaurbana.viewmodel.AuthUiState

@Composable
fun RegisterScreen(
    navController: NavController,
    vm: AuthViewModel = viewModel()
) {
    val ui: AuthUiState by vm.ui.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isFormValid = username.isNotBlank() &&
            email.contains("@") &&
            password.length >= 4 &&
            password == confirmPassword

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (ui.error != null) {
                Text(
                    ui.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    vm.register(username, email, password)
                },
                enabled = isFormValid && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (ui.isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                else
                    Text("Registrar")
            }

            TextButton(onClick = {
                navController.navigate(Route.Login.path) {
                    popUpTo(Route.Register.path) { inclusive = true }
                }
            }) {
                Text("¿Ya tienes una cuenta? Inicia sesión")
            }

            // Cuando el registro sea exitoso (en el futuro)
            if (ui.success) {
                LaunchedEffect(Unit) {
                    navController.navigate(Route.Login.path) {
                        popUpTo(Route.Register.path) { inclusive = true }
                    }
                }
            }
        }
    }
}
