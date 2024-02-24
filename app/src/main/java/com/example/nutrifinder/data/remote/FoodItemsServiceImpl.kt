package com.example.nutrifinder.data.remote

import android.util.Log
import com.example.nutrifinder.data.remote.dto.FoodItemResponse
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

    override suspend fun getFoodItems(queryText: String): List<FoodItemResponse> {
        return try {
            val response: List<FoodItemResponse> = client.get {
                url {
                    host = HttpRoutes.BASE_URL
                    path(HttpRoutes.PATH_SEARCH)
                    protocol = URLProtocol.HTTPS
                    parameters.append(NetworkConstants.FOOD_ITEMS_PARAMETER, queryText)
                }
            }.body()

            response
        } catch (e: RedirectResponseException) {
            // Handle 3xx codes
            Log.d("HelloWorld", "getFoodItems 3xx: ${e.localizedMessage}")
            emptyList()
        } catch (e: ClientRequestException) {
            // Handle 4xx codes
            Log.d("HelloWorld", "getFoodItems 4xx: ${e.localizedMessage}")
            emptyList()
        } catch (e: ServerResponseException) {
            // Handle 5xx codes
            Log.d("HelloWorld", "getFoodItems 5xx: ${e.localizedMessage}")
            emptyList()
        } catch (e: Exception) {
            // Handle other exceptions
            Log.d("HelloWorld", "getFoodItems else: ${e.localizedMessage}")
            emptyList()
        }
    }
}