package com.example.nutrifinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrifinder.data.remote.FoodItemsService
import com.example.nutrifinder.data.remote.dto.FoodItemResponse
import com.example.nutrifinder.designsystem.theme.NutriFinderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var foodItemsService: FoodItemsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val foodItems = produceState<List<FoodItemResponse>>(
                initialValue = emptyList(),
                producer = {
                    value = foodItemsService.getFoodItems("chicken")
                }
            )
            NutriFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(foodItems.value) { foodItem ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = "${foodItem.id}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${foodItem.brand}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${foodItem.name}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${foodItem.calories}")
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${foodItem.portion}")
                            }
                        }
                    }
                }
            }
        }
    }
}