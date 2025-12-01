package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: AuthViewModel
) {
    val ui by vm.ui.collectAsState()

    // Guardamos valores en variables locales para evitar el error del smart cast
    val nombre = ui.user?.name
    val email = ui.user?.email

    // Texto más bonito si el nombre viene nulo
    val displayName = when {
        !nombre.isNullOrBlank() -> nombre
        !email.isNullOrBlank() -> email.substringBefore("@")
        else -> "cliente"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Bienvenido, $displayName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Correo: ${email ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Route.ProductList.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Ver catálogo")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    vm.logout()
                    navController.navigate(Route.Login.route) {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
