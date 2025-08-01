package com.example.ecartapp.model

data class CategoryModel(
    val category: String="",
    val description: String="",
    val id: String="",
    val image: List<String> = emptyList(),
    val name: String="",
    val price: String="",
    val realprice: String="",
    val otherdetails: Map<String, String> = mapOf()
)
