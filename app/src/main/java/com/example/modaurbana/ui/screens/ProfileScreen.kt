package com.example.modaurbana.ui.screens


import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.content.FileProvider

@Composable
fun ProfileScreen(
    navController: NavHostController,
    vm: AuthViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val avatar by vm.avatarUriFlow.collectAsState(initial = "")

    LaunchedEffect(ui.user) {
        if (ui.user == null && ui.error == null) {
            navController.navigate(Route.Login.route) { popUpTo(0) }
        }
    }

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { picked: Uri? ->
        picked?.let {
            scope.launch { vm.saveAvatarUri(it.toString()) }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            tempCameraUri?.let { uri ->
                scope.launch { vm.saveAvatarUri(uri.toString()) }
            }
        }
    }

    fun createImageUriForCamera(context: Context): Uri {
        val imagesDir = File(context.cacheDir, "images").apply { if (!exists()) mkdirs() }
        val file = File.createTempFile("avatar_", ".jpg", imagesDir)
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(avatar.ifBlank { null })
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Text("Desde galería")
                    }
                    OutlinedButton(onClick = {
                        val uri = createImageUriForCamera(context).also { tempCameraUri = it }
                        cameraLauncher.launch(uri)
                    }) {
                        Text("Tomar foto")
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text("Nombre: ${ui.user?.name ?: "-"}")
                Text("Correo: ${ui.user?.email ?: "-"}")

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        vm.logout()
                        navController.navigate(Route.Login.route) { popUpTo(0) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) { Text("Cerrar sesión") }
            }
        }
    }
}
