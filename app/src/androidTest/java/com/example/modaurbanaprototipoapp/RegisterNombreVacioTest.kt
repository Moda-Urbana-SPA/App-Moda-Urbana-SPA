package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.RegisterViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterNombreVacioTest {

    @Test
    fun nombreVacioMarcaNameError() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = RegisterViewModel(app)

        viewModel.onEmailChange("test@example.com")
        viewModel.onNameChange("")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")

        viewModel.register()

        val state = viewModel.uiState.value

        assertNotNull("Debe haber error en name cuando está vacío", state.nameError)
        assertFalse(
            "No debería marcar registro exitoso con nombre vacío",
            state.isRegistrationSuccessful
        )
    }
}
