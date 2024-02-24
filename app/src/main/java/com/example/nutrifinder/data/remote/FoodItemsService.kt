package com.example.nutrifinder.data.remote

import com.example.nutrifinder.data.remote.dto.FoodItemResponse

interface FoodItemsService {

    suspend fun getFoodItems(queryText: String): List<FoodItemResponse>
}