package com.example.modaurbana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.modaurbana.ui.navigation.AppNavHost
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.ui.theme.ModaUrbanaTheme
import com.example.modaurbana.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModaUrbanaTheme {
                val navController = rememberNavController()
                val vm: AuthViewModel = viewModel()
                val ui by vm.ui.collectAsState()

                val start = if (ui.loggedUser != null) Route.Home.route else Route.Login.route
                AppNavHost(navController = nav, startDestination = start)

            }
        }
    }
}
