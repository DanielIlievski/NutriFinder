package com.example.nutrifinder.mock

import com.example.nutrifinder.data.network.FoodItemsService
import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.util.ApiResult
import javax.inject.Inject

class MockFoodItemsService @Inject constructor() : FoodItemsService {
    override suspend fun getFoodItems(queryText: String): ApiResult<List<FoodItem>> {
        return if (queryText == "chicken") {
            ApiResult.Success(
                listOf(
                    FoodItem(0, "Journal Communications", "BBQ Chicken Pizza", 119, 231)
                )
            )
        } else if (queryText.isBlank()) {
            ApiResult.Failure.Exception("`kv` must be at least three characters")
        } else {
            ApiResult.Success(emptyList())
        }
    }
}