package com.example.nutrifinder.data.network.dto

import com.example.nutrifinder.domain.model.FoodItem
import kotlinx.serialization.Serializable

@Serializable
data class FoodItemResponse(
    val id: Int,
    val brand: String,
    val name: String,
    val calories: Int,
    val portion: Int
)

fun FoodItemResponse.asExternalModel(): FoodItem {
    return FoodItem(
        id = this.id,
        brand = this.brand,
        name = this.name,
        calories = this.calories,
        portion = this.portion
    )
}