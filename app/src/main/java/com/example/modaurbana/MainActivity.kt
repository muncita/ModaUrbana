package com.example.modaurbana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.modaurbana.ui.navigation.AppNavGraph
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    // ✅ ViewModel en scope de Activity (usa AndroidViewModelFactory bajo el capó)
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Decide inicio en tiempo de ejecución
                    var start by remember { mutableStateOf<String?>(null) }

                    // Puedes hacer lógica de arranque aquí (leer token, etc.)
                    LaunchedEffect(Unit) {
                        try {
                            vm.loadUser()
                            // Si quieres decidir según token, aquí puedes consultarlo en tu SessionManager.
                        } finally {
                            start = Route.Login.route   // cámbialo a Home si quieres auto-login
                        }
                    }

                    AppNavGraph(
                        vm = vm,
                        startDestination = start ?: Route.Login.route
                    )
                }
            }
        }
    }
}
