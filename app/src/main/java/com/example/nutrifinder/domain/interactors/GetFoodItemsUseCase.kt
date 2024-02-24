package com.example.nutrifinder.domain.interactors

import com.example.nutrifinder.data.network.FoodItemsService
import javax.inject.Inject

class GetFoodItemsUseCase @Inject constructor(
    private val foodItemsService: FoodItemsService
) {

    suspend operator fun invoke(queryText: String) = foodItemsService.getFoodItems(queryText)
}