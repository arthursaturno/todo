package com.example.mylist.core.route

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object TodoList : AppRoute("todo_list")
}
