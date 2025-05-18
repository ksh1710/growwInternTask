package com.example.growwtask.utils


sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    data class Success<T>(val data: T?) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}