package com.example.mylist.domain.model

enum class Priority(val label: String, val order: Int) {
    LOW(label = "Baixa", order = 3),
    MEDIUM(label = "Média", order = 2),
    HIGH(label = "Alta", order = 1)
}
