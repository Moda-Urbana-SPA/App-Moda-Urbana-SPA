package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadUser(id: Int = 1) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            val result = repository.getUserById(id)

            _uiState.value = result.fold(
                onSuccess = { user ->
                    _uiState.value.copy(
                        isLoading = false,
                        userName = "${user.firstName} ${user.lastName}",
                        userEmail = user.email,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Error desconocido"
                    )
                }
            )
        }
    }
}