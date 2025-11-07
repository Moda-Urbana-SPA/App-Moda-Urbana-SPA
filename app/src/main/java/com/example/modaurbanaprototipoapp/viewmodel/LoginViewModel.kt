package com.example.modaurbanaprototipoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.modaurbanaprototipoapp.data.local.SessionManager
import com.example.modaurbanaprototipoapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoginSuccessful: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    private fun validateForm(): Boolean {
        var isValid = true
        val currentState = _uiState.value

        if (currentState.email.isBlank()) {
            _uiState.value = currentState.copy(emailError = "El email es requerido")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.value = currentState.copy(emailError = "Email inválido")
            isValid = false
        }

        if (currentState.password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = "La contraseña es requerida")
            isValid = false
        }

        return isValid
    }

    fun login() {
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

                val result = repository.login(email, password)

                result.fold(
                    onSuccess = { response ->
                        // Guardar token y email
                        sessionManager.saveAuthToken(response.authToken)
                        sessionManager.saveDummyUserSession(
                            username = response.email ?: email,
                            token = response.authToken
                        )

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = when {
                                exception.message?.contains("401") == true ->
                                    "Email o contraseña incorrectos"
                                exception.message?.contains("Unable to resolve host") == true ->
                                    "Sin conexión a internet"
                                else ->
                                    exception.localizedMessage ?: "Error al iniciar sesión"
                            }
                        )
                    }
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            val token = sessionManager.getAuthToken()
            if (!token.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(isLoginSuccessful = true)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy(isLoginSuccessful = false)
    }
}