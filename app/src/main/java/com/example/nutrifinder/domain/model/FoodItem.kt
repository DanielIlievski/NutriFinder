package com.example.nutrifinder.domain.model

data class FoodItem(
    val id: Int,
    val brand: String,
    val name: String,
    val calories: Int,
    val portion: Int
)