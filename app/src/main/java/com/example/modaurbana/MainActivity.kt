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

    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    var start by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(Unit) {
                        try {
                            vm.loadUser()
                        } finally {
                            start = Route.Login.route
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
