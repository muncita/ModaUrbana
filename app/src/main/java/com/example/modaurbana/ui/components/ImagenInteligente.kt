package com.example.modaurbana.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Componente de avatar reutilizable (Guía 8/10).
 * Completa estilos/animaciones con Guía 12 (AnimatedVisibility, etc.) si quieres sumar puntaje EP3.
 */
@Composable
fun ImagenInteligente(uri: Uri?, onClick: () -> Unit) {
    Box {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = "Avatar",
                modifier = Modifier.size(120.dp).clip(CircleShape).clickable { onClick() }
            )
        } else {
            Surface(
                modifier = Modifier.size(120.dp).clip(CircleShape).clickable { onClick() }
            ) {
                Icon(Icons.Default.Person, contentDescription = "Seleccionar avatar")
            }
        }
    }
}
