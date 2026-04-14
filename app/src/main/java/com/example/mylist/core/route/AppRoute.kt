package com.example.mylist.navigation

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object TodoList : AppRoute("todo_list")
}
