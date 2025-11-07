package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.data.remote.dto.UserDto
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository = UserRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadCurrentUserProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result: Result<UserDto> = repository.getCurrentUser()

                result.fold(
                    onSuccess = { user: UserDto ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = user,
                            error = null
                        )
                    },
                    onFailure = { exception: Throwable ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = when {
                                exception.message?.contains("401") == true ->
                                    "Sesión expirada. Por favor inicia sesión nuevamente"
                                exception.message?.contains("Unable to resolve host") == true ->
                                    "Sin conexión a internet"
                                else -> exception.localizedMessage ?: "Error al cargar perfil"
                            }
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error al cargar perfil"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
