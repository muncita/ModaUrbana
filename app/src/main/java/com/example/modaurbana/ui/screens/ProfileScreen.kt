package com.example.modaurbana.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.viewmodel.ProfileUiState
import com.example.modaurbana.viewmodel.ProfileViewModel
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val state: ProfileUiState by viewModel.uiState.collectAsState()

    // Cargar datos al entrar (usa id fijo 1 por ahora; cuando tengas /me lo cambias)
    LaunchedEffect(Unit) { viewModel.loadUser(1) }

    // Launcher para elegir imagen de galería (funciona sin permisos adicionales)
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateAvatar(uri)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❌ Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser(1) }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)

                    // Avatar simple; usa Coil si lo tienes
                    Box(contentAlignment = Alignment.Center) {
                        val avatar = state.avatarUri
                        if (avatar != null) {
                            AsyncImage(
                                 model = avatar,
                                 contentDescription = "Avatar",
                                 modifier = Modifier
                                     .size(120.dp)
                                     .clip(CircleShape)
                                     .clickable { pickImageLauncher.launch("image/*") }
                             )
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .clickable { pickImageLauncher.launch("image/*") },
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {}
                        } else {
                            Surface(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .clickable { pickImageLauncher.launch("image/*") },
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {}
                        }
                    }

                    // Nombre
                    InfoCard(title = "Nombre", value = state.userName)

                    // Email
                    InfoCard(title = "Email", value = state.userEmail)

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { viewModel.loadUser(1) }) {
                            Text("Refrescar")
                        }
                        OutlinedButton(onClick = {
                            viewModel.logout {
                                // Aquí no navegamos; lo haces desde Navigation o Main si quieres
                            }
                        }) {
                            Text("Cerrar sesión")
                        }
                    }

                    // TODO (más adelante): Cámara con FileProvider + TakePicture()
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
