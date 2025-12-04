package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.RegisterViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterPasswordsDistintasTest {

    @Test
    fun contrasenasDistintasMarcanConfirmPasswordError() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = RegisterViewModel(app)

        viewModel.onEmailChange("test@example.com")
        viewModel.onNameChange("Usuario")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password000")

        viewModel.register()

        val state = viewModel.uiState.value

        assertNotNull(
            "Debe haber error en confirmPassword cuando las contraseñas no coinciden",
            state.confirmPasswordError
        )
        assertFalse(
            "No debería marcar registro exitoso con contraseñas distintas",
            state.isRegistrationSuccessful
        )
    }
}
