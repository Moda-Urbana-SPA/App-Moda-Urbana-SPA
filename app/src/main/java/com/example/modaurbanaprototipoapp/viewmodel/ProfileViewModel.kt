package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.data.remote.dto.UserDto
import com.example.modaurbanaprototipoapp.repository.AvatarRepository
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: UserDto? = null,
    val error: String? = null,
    val formattedCreatedAt: String = "",
    val avatarUri: Uri? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = UserRepository(application)
    private val avatarRepository: AvatarRepository = AvatarRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadSavedAvatar()
    }

    private fun loadSavedAvatar() {
        viewModelScope.launch {
            avatarRepository.getAvatarUri().collect { savedUri ->
                _uiState.value = _uiState.value.copy(avatarUri = savedUri)
            }
        }
    }

    fun loadCurrentUserProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val result = userRepository.getCurrentUser()

                if (result.isSuccess) {
                    val user = result.getOrThrow()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        error = null
                    )
                } else {
                    val exception = result.exceptionOrNull()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = when {
                            exception?.message?.contains("401") == true ->
                                "Sesión expirada. Por favor inicia sesión nuevamente"
                            exception?.message?.contains("Unable to resolve host") == true ->
                                "Sin conexión a internet"
                            else ->
                                exception?.localizedMessage ?: "Error al cargar perfil"
                        }
                    )
                }
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

    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
        }
    }

    fun clearAvatar() {
        viewModelScope.launch {
            avatarRepository.clearAvatarUri()
            _uiState.value = _uiState.value.copy(avatarUri = null)
        }
    }
}
