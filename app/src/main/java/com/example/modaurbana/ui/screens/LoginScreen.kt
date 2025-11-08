package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

@Composable
fun LoginScreen(nav: NavHostController, vm: AuthViewModel = viewModel()) {
    val ui by vm.ui.collectAsState()

    // Si ya hay sesión, navega a perfil
    LaunchedEffect(ui.loggedUser) {
        if (ui.loggedUser != null) {
            nav.navigate(Route.Home.route) {
                popUpTo(Route.Login.route) { inclusive = true }
            }
        }
    }


    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = ui.email, onValueChange = vm::onEmail, label = { Text("Email") },
            isError = ui.emailError != null, supportingText = { ui.emailError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ui.password, onValueChange = vm::onPass, label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = ui.passError != null, supportingText = { ui.passError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        if (ui.errorMessage != null) Text(ui.errorMessage!!, color = MaterialTheme.colorScheme.error)

        Button(
            onClick = { vm.doLogin { /* navegación en LaunchedEffect */ } },
            enabled = !ui.loading,
            modifier = Modifier.fillMaxWidth()
        ) { if (ui.loading) CircularProgressIndicator(strokeWidth = 2.dp) else Text("Ingresar") }

        TextButton(onClick = { nav.navigate(Route.Register.route) }) { Text("Crear cuenta") }
    }
}
