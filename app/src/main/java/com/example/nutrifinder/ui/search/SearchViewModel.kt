package com.example.nutrifinder.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrifinder.domain.interactors.GetFoodItemsUseCase
import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.util.UiState
import com.example.nutrifinder.util.suspendOnError
import com.example.nutrifinder.util.suspendOnException
import com.example.nutrifinder.util.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getFoodItemsUseCase: GetFoodItemsUseCase
) : ViewModel() {

    private val _searchFormState = MutableStateFlow(SearchFormState.Empty)
    val searchFormState = _searchFormState.asStateFlow()

    private val _searchResult = MutableSharedFlow<UiState>()
    val searchResult = _searchResult.asSharedFlow()

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.QueryTextChanged -> {
                _searchFormState.value = _searchFormState.value.copy(queryText = event.queryText)
                getFoodItems()
            }
        }
    }

    private fun getFoodItems() = viewModelScope.launch(Dispatchers.IO) {
        if (validateQueryText()) {
            viewModelScope.launch(Dispatchers.Main) {
                _searchResult.emit(UiState.Loading)
            }

            getFoodItemsUseCase.invoke(_searchFormState.value.queryText)
                .suspendOnSuccess {
                    _searchFormState.value = _searchFormState.value.copy(foodItems = this.data)
                    _searchResult.emit(UiState.Success)
                }
                .suspendOnError {
                    _searchResult.emit(UiState.Error(message))
                }
                .suspendOnException {
                    _searchResult.emit(UiState.Error(message))
                }
        } else {
            _searchFormState.value = _searchFormState.value.copy(foodItems = emptyList())
        }
    }

    private fun validateQueryText(): Boolean {
        val isTextValid =
            _searchFormState.value.queryText.isNotBlank() && _searchFormState.value.queryText.length > 2
        _searchFormState.value = _searchFormState.value.copy(isQueryTextValid = isTextValid)

        return isTextValid
    }
}

data class SearchFormState(
    val queryText: String,
    val foodItems: List<FoodItem>,
    val isQueryTextValid: Boolean? = null
) {
    companion object {
        val Empty = SearchFormState(
            queryText = "",
            foodItems = emptyList()
        )
    }
}

sealed class SearchUiEvent {
    data class QueryTextChanged(val queryText: String) : SearchUiEvent()
}