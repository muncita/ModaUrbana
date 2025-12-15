package com.example.modaurbana.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modaurbana.ui.screens.CartScreen
import com.example.modaurbana.ui.screens.HomeScreen
import com.example.modaurbana.ui.screens.LoginScreen
import com.example.modaurbana.ui.screens.ProductDetailScreen
import com.example.modaurbana.ui.screens.ProductListScreen
import com.example.modaurbana.ui.screens.ProfileScreen
import com.example.modaurbana.ui.screens.RegisterScreen
import com.example.modaurbana.viewmodel.AuthViewModel
import com.example.modaurbana.viewmodel.CartViewModel
import com.example.modaurbana.viewmodel.ProductListViewModel

@Composable
fun AppNavGraph(
    vm: AuthViewModel,
    startDestination: String
) {
    val navController = rememberNavController()
    val cartVm: CartViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            val showBar = currentDestination?.route in listOf(
                Route.Home.route,
                Route.ProductList.route,
                Route.Cart.route,
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
                ProductListScreen(
                    navController = navController,
                    productListViewModel = productVm,
                    cartViewModel = cartVm
                )
            }

            composable(Route.Cart.route) {
                CartScreen(
                    navController = navController,
                    vm = cartVm
                )
            }

            composable(
                route = Route.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId").orEmpty()

                ProductDetailScreen(
                    navController = navController,
                    productId = productId,
                    cartViewModel = cartVm
                )
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
            BottomItem("Inicio", Route.Home.route, Icons.Filled.Home),
            BottomItem("Catálogo", Route.ProductList.route, Icons.Filled.ShoppingCart),
            BottomItem("Perfil", Route.Profile.route, Icons.Filled.Person),
        )

        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

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
