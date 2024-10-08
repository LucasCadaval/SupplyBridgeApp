package com.pi.supplybridge.presentation.ui.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    data object ForgotPassword : Screen("forgot_password")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object Profile : Screen("profile")
    data object NewOrder : Screen("new_order")
    data object Home : Screen("home")

}
