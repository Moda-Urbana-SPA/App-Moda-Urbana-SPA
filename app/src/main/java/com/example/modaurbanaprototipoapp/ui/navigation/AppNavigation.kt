package com.example.modaurbanaprototipoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modaurbanaprototipoapp.ui.screens.HomeScreen
import com.example.modaurbanaprototipoapp.ui.screens.LoginScreen
import com.example.modaurbanaprototipoapp.ui.screens.ProfileScreen
import com.example.modaurbanaprototipoapp.ui.screens.RegisterScreen
import com.example.modaurbanaprototipoapp.data.local.SessionManager
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Int) = "profile/$userId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    val ctx = LocalContext.current
    val session = SessionManager(ctx)
    val scope = rememberCoroutineScope()

    fun navigateToLoginClearingBackstack() {
        navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                },
                onLogout = {
                    scope.launch {
                        session.clearAuthToken()
                        navigateToLoginClearingBackstack()
                    }
                }
            )
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    scope.launch {
                        session.clearAuthToken()
                        navigateToLoginClearingBackstack()
                    }
                }
            )
        }
    }
}
