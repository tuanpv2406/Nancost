package com.example.nancost.model

data class User(
    val username: String,
    val password: String,
    val level: String = "user"
)