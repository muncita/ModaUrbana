package com.example.modaurbana.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.modaurbana.ui.screens.HomeScreen
import com.example.modaurbana.ui.screens.LoginScreen
import com.example.modaurbana.ui.screens.ProfileScreen
import com.example.modaurbana.ui.screens.RegisterScreen
import com.example.modaurbana.viewmodel.AuthViewModel
import com.example.modaurbana.ui.screens.ProductListScreen
import com.example.modaurbana.ui.screens.CartScreen
import com.example.modaurbana.viewmodel.ProductListViewModel
import com.example.modaurbana.viewmodel.CartViewModel


@Composable
fun AppNavGraph(
    vm: AuthViewModel,
    startDestination: String
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentDestination = navController.currentDestination()
            val showBar = currentDestination?.route in listOf(
                Route.Home.route,
                Route.Profile.route
            )
            if (showBar) {
                BottomBar(navController, currentDestination)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Login.route) {
                LoginScreen(navController = navController, vm = vm)
            }
            composable(Route.Register.route) {
                RegisterScreen(
                    navController = navController,
                    vm = vm,
                    onRegisterSuccess = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Route.Home.route) {
                HomeScreen(navController = navController, vm = vm)
            }
            composable(Route.Profile.route) {
                ProfileScreen(navController = navController, vm = vm)

            }

            composable(Route.ProductList.route) {
                val productVm: ProductListViewModel = viewModel()
                val cartVm: CartViewModel = viewModel()
                ProductListScreen(
                    navController = navController,
                    productListViewModel = productVm,
                    cartViewModel = cartVm
                )
            }

            composable(Route.Cart.route) {
                val cartVm: CartViewModel = viewModel()
                CartScreen(navController = navController, vm = cartVm)
            }

        }
        }
    }

@Composable
private fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        val items = listOf(
            BottomItem("Inicio", Route.Home.route, Icons.Default.Home),
            BottomItem("Perfil", Route.Profile.route, Icons.Default.Person),
        )
        items.forEach { item ->
            val selected = currentDestination.isInHierarchy(item.route)
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

private data class BottomItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
private fun NavHostController.currentDestination(): NavDestination? {
    val backStackEntry by this.currentBackStackEntryAsState()
    return backStackEntry?.destination
}

private fun NavDestination?.isInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
