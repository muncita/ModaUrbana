package com.example.modaurbana.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modaurbana.R
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: AuthViewModel
) {
    val ui by vm.ui.collectAsState()

    val nombre = ui.user?.name
    val email = ui.user?.email

    val displayName = when {
        !nombre.isNullOrBlank() -> nombre
        !email.isNullOrBlank() -> email.substringBefore("@")
        else -> "cliente"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            HomeTopBar()

            HomeContent(
                displayName = displayName,
                email = email,
                onVerCatalogo = {
                    navController.navigate(Route.ProductList.route)
                },
                onCerrarSesion = {
                    vm.logout()
                    navController.navigate(Route.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Spacer(Modifier.weight(1f))




        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

        }
    }
}

@Composable
private fun HomeContent(
    displayName: String,
    email: String?,
    onVerCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(Modifier.height(32.dp))

        // Texto de bienvenida
        Text(
            text = "Bienvenido, $displayName",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )


        // Hero grande con la foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .height(340.dp)
        ) {


            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nueva colección urbana",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Descubre las prendas seleccionadas para ti.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(Modifier.height(24.dp))


        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .height(260.dp)
        ) {

            // 🟣 Overlay suave
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nuestra historia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "ModaUrbana nace del deseo de acercar prendas únicas, difíciles de encontrar y seleccionadas desde rincones donde la moda independiente vive y evoluciona. " +
                            "Una plataforma creada para quienes buscan algo distinto, auténtico y con carácter.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4
                )
            }
        }


    }
}
