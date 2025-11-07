package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.data.local.SessionManager
import com.example.modaurbanaprototipoapp.data.local.database.AppDatabase
import com.example.modaurbanaprototipoapp.data.local.entity.ClothingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val clothingItems: List<ClothingItem> = emptyList(),
    val filteredItems: List<ClothingItem> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val error: String? = null,
    val isLoggedOut: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val clothingDao = database.clothingDao()
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadClothingItems()
    }

    private fun loadClothingItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                clothingDao.getAllClothing().collect { items ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        clothingItems = items,
                        filteredItems = items,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar productos: ${e.localizedMessage}"
                )
            }
        }
    }

    fun filterByCategory(category: String?) {
        val currentState = _uiState.value

        val filtered = if (category == null) {
            currentState.clothingItems
        } else {
            currentState.clothingItems.filter { it.category == category }
        }

        _uiState.value = currentState.copy(
            selectedCategory = category,
            filteredItems = filtered
        )
    }

    fun searchItems(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        val currentState = _uiState.value
        val filtered = if (query.isBlank()) {
            if (currentState.selectedCategory != null) {
                currentState.clothingItems.filter { it.category == currentState.selectedCategory }
            } else {
                currentState.clothingItems
            }
        } else {
            currentState.clothingItems.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = currentState.copy(filteredItems = filtered)
    }

    fun logout() {
        viewModelScope.launch {
            try {
                sessionManager.clearAuthToken()
                _uiState.value = _uiState.value.copy(isLoggedOut = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cerrar sesión"
                )
            }
        }
    }

    fun refresh() {
        loadClothingItems()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}