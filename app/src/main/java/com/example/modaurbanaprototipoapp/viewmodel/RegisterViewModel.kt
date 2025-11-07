package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false,
    val emailError: String? = null,
    val nameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null
        )
    }

    private fun validateForm(): Boolean {
        var isValid = true
        val s = _uiState.value

        // Email
        if (s.email.isBlank()) {
            _uiState.value = s.copy(emailError = "El email es requerido"); isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) {
            _uiState.value = s.copy(emailError = "Email inválido"); isValid = false
        }

        // Nombre
        if (s.name.isBlank()) {
            _uiState.value = s.copy(nameError = "El nombre es requerido"); isValid = false
        } else if (s.name.length < 3) {
            _uiState.value = s.copy(nameError = "Mínimo 3 caracteres"); isValid = false
        }

        // Contraseña (mínimo 8 y al menos 1 letra)
        val hasMinLen = s.password.length >= 8
        val hasLetter = s.password.any { it.isLetter() }

        if (s.password.isBlank()) {
            _uiState.value = s.copy(passwordError = "La contraseña es requerida"); isValid = false
        } else if (!hasMinLen) {
            _uiState.value = s.copy(passwordError = "Mínimo 8 caracteres"); isValid = false
        } else if (!hasLetter) {
            _uiState.value = s.copy(passwordError = "Debe incluir al menos 1 letra"); isValid = false
        }

        // Confirmación
        if (s.confirmPassword != s.password) {
            _uiState.value = s.copy(confirmPasswordError = "Las contraseñas no coinciden"); isValid = false
        }

        return isValid
    }

    fun register() {
        if (!validateForm()) {
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                val email = _uiState.value.email
                val password = _uiState.value.password
                val name = _uiState.value.name

                val result = repository.signup(email, password, name)

                result.fold(
                    onSuccess = { response ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = when {
                                exception.message?.contains("409") == true ||
                                        exception.message?.contains("already exists") == true ->
                                    "Este email ya está registrado"
                                exception.message?.contains("Unable to resolve host") == true ->
                                    "Sin conexión a internet"
                                else ->
                                    exception.localizedMessage ?: "Error al registrar usuario"
                            }
                        )
                    }
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error al registrar usuario"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetRegistrationSuccess() {
        _uiState.value = _uiState.value.copy(isRegistrationSuccessful = false)
    }
}