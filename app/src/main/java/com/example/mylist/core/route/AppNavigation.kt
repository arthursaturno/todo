package com.example.mylist.core.route
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylist.feature.login.LoginScreen
import com.example.mylist.feature.todo.TodoListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.route
    ) {
        composable(AppRoute.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoute.TodoList.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoute.TodoList.route) {
            TodoListScreen(
                onLogout = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.TodoList.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
