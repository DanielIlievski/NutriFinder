package com.example.nutrifinder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemResponse(
    val id: Int,
    val brand: String,
    val name: String,
    val calories: Int,
    val portion: Int
)
