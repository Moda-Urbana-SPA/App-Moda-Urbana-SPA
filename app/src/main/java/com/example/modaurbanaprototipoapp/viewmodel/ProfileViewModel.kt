package com.example.modaurbanaprototipoapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.data.remote.dto.UserDto
import com.example.modaurbanaprototipoapp.repository.AvatarRepository
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: UserDto? = null,
    val error: String? = null,
    val formattedCreatedAt: String = "",
    val avatarUri: Uri? = null
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val avatarRepository: AvatarRepository
) : ViewModel() {

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
                        user = user
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al cargar perfil"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado"
                )
            }
        }
    }

    fun updateAvatar(uri: Uri?) {
        viewModelScope.launch {
            avatarRepository.saveAvatarUri(uri)
            _uiState.value = _uiState.value.copy(avatarUri = uri)
        }
    }

    fun clearAvatar() {
        viewModelScope.launch {
            avatarRepository.clearAvatarUri()
            _uiState.value = _uiState.value.copy(avatarUri = null)
        }
    }
}
