package com.example.modaurbana.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel()
) {
    // <- usa lifecycle-runtime-compose y el import de getValue
    val ui by vm.ui.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Bienvenido/a ${ui.loggedUser?.name ?: ""}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(20.dp))

        Button(onClick = { nav.navigate(Route.Profile.route) }) {
            Text("Ir a mi perfil")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                vm.doLogout()            // ← basta con llamar la función del VM
                nav.navigate(Route.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Cerrar sesión")
        }
    }
}
