package com.example.mylist.core.utils

fun formatDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    return buildString {
        digits.forEachIndexed { index, c ->
            if (index == 2 || index == 4) append('/')
            append(c)
        }
    }
}
