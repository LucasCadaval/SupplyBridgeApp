package com.pi.supplybridge.presentation.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pi.supplybridge.presentation.ui.screens.DashboardScreen
import com.pi.supplybridge.presentation.ui.screens.ForgotPasswordScreen
import com.pi.supplybridge.presentation.ui.screens.HomeScreen
import com.pi.supplybridge.presentation.ui.screens.LoginScreen
import com.pi.supplybridge.presentation.ui.screens.NewOrderScreen
import com.pi.supplybridge.presentation.ui.screens.OrderDetailScreen
import com.pi.supplybridge.presentation.ui.screens.ProfileScreen
import com.pi.supplybridge.presentation.ui.screens.RegisterScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(route = Screen.OrderDetail.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(onLoginClick = { navController.navigate(Screen.Login.route) })
        }

        composable(route = Screen.NewOrder.route) {
            NewOrderScreen(navController)
        }

        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
