package com.example.nutrifinder.data.network

import com.example.nutrifinder.data.network.dto.FoodItemResponse
import com.example.nutrifinder.data.network.dto.asExternalModel
import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.util.ApiResult
import com.example.nutrifinder.util.HttpRoutes
import com.example.nutrifinder.util.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import javax.inject.Inject

class FoodItemsServiceImpl @Inject constructor(
    private val client: HttpClient
): FoodItemsService {

    override suspend fun getFoodItems(queryText: String): ApiResult<List<FoodItem>> {
        return try {
            val response: List<FoodItemResponse> = client.get {
                url {
                    host = HttpRoutes.BASE_URL
                    path(HttpRoutes.PATH_SEARCH)
                    protocol = URLProtocol.HTTPS
                    parameters.append(NetworkConstants.FOOD_ITEMS_PARAMETER, queryText)
                }
            }.body()

            ApiResult.Success(response.map { it.asExternalModel() })
        } catch (e: RedirectResponseException) {
            // Handle 3xx codes
            e.printStackTrace()
            ApiResult.Failure.Error(e.response.status, e.localizedMessage ?: e.message)
        } catch (e: ClientRequestException) {
            // Handle 4xx codes
            e.printStackTrace()
            ApiResult.Failure.Error(e.response.status, e.localizedMessage ?: e.message)
        } catch (e: ServerResponseException) {
            // Handle 5xx codes
            e.printStackTrace()
            ApiResult.Failure.Error(e.response.status, e.localizedMessage ?: e.message)
        } catch (e: Exception) {
            // Handle other exceptions
            e.printStackTrace()
            ApiResult.Failure.Exception(e.localizedMessage ?: e.message ?: "Unknown exception")
        }
    }
}