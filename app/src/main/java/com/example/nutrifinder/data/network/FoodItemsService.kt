package com.example.nutrifinder.data.network

import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.util.ApiResult

interface FoodItemsService {

    suspend fun getFoodItems(queryText: String): ApiResult<List<FoodItem>>
}