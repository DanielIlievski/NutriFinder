package com.example.nutrifinder.ui.search

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutrifinder.R
import com.example.nutrifinder.designsystem.theme.NutriFinderTheme
import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.util.UiState

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchFormState by viewModel.searchFormState.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle(UiState.Empty)
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SearchScreen(
        modifier = modifier,
        searchFormState = searchFormState,
        onQueryTextChanged = viewModel::onEvent,
        onFoodItemClick = { itemName ->
            Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show()
        },
        isLoading = isLoading
    )

    LaunchedEffect(key1 = searchResult) {
        when (searchResult) {
            UiState.Empty -> {}
            is UiState.Error -> {
                Toast.makeText(context, (searchResult as UiState.Error).error, Toast.LENGTH_SHORT)
                    .show()
                isLoading = false
            }

            UiState.Loading -> {
                isLoading = true
            }

            UiState.Success -> {
                isLoading = false
            }
        }
    }
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    searchFormState: SearchFormState,
    isLoading: Boolean,
    onQueryTextChanged: (SearchUiEvent) -> Unit,
    onFoodItemClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_12))
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchFormState.queryText,
            onValueChange = { text ->
                onQueryTextChanged(SearchUiEvent.QueryTextChanged(text))
            },
            label = { Text(text = stringResource(R.string.txt_label_food_item)) },
            placeholder = { Text(text = stringResource(R.string.txt_placeholder_food_item)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isError = searchFormState.isQueryTextValid == false,
            supportingText = {
                if (searchFormState.isQueryTextValid == false) {
                    Text(text = stringResource(R.string.err_invalid_query))
                }
            },
            trailingIcon = {
                if (searchFormState.queryText.isNotEmpty()) {
                    IconButton(onClick = { onQueryTextChanged(SearchUiEvent.QueryTextChanged("")) }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.content_description_clear_text)
                        )
                    }
                }
            }
        )

        AnimatedVisibility(searchFormState.queryText.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(
                    bottomStart = dimensionResource(R.dimen.corner_8),
                    bottomEnd = dimensionResource(R.dimen.corner_8)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = dimensionResource(R.dimen.elevation_3)
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    state = rememberLazyListState()
                ) {
                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_12))
                                    .fillMaxWidth()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.size_20))
                                        .align(Alignment.Center),
                                )
                            }
                        }
                    } else if (searchFormState.foodItems.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(dimensionResource(R.dimen.padding_12)),
                                text = stringResource(R.string.txt_food_items_empty)
                            )
                        }
                    }
                    itemsIndexed(searchFormState.foodItems) { index, foodItem ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp)
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_8)))
                                .clickable { onFoodItemClick(foodItem.name) }
                                .padding(dimensionResource(R.dimen.padding_12)),
                            text = foodItem.name,
                        )
                        if (index < searchFormState.foodItems.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_12)),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    NutriFinderTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            SearchScreen(
                searchFormState = SearchFormState(
                    queryText = "chicken",
                    foodItems = listOf(
                        FoodItem(1, "Brand 1", "Name 1", 111, 111),
                        FoodItem(2, "Brand 2", "Name 2", 222, 222),
                        FoodItem(3, "Brand 3", "Name 3", 333, 333),
                    )
                ),
                onQueryTextChanged = { },
                onFoodItemClick = { },
                isLoading = false
            )
        }
    }
}