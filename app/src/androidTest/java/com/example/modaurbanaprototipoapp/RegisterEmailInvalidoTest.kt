package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.RegisterViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterEmailInvalidoTest {

    @Test
    fun emailInvalidoMarcaErrorEnRegistro() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = RegisterViewModel(app)

        viewModel.onEmailChange("correo-malo")
        viewModel.onNameChange("Usuario")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")

        viewModel.register()

        val state = viewModel.uiState.value

        assertNotNull("Debe haber error en email cuando el formato es inválido", state.emailError)
        assertFalse(
            "No debería marcar registro exitoso con email inválido",
            state.isRegistrationSuccessful
        )
    }
}
