package com.example.ecartapp.model

data class UserModel(
    val name: String="",
    val email: String="",
    val uid: String="",
    val cartItems: Map<String, Long> = mapOf(),
    val address: String="",
    val phoneNumber: String=""
)
