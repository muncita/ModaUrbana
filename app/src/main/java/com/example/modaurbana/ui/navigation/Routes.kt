package com.example.modaurbana.ui.navigation

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Profile : Route("profile")
}
