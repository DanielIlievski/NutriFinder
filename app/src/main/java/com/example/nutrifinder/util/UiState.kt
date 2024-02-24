package com.example.nutrifinder.util

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val error: String) : UiState()
    data object Empty : UiState()
}