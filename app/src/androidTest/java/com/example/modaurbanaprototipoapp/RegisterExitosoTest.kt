package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.RegisterViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterExitosoTest {

    @Test
    fun registroValidoNoGeneraErrores() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = RegisterViewModel(app)

        viewModel.onEmailChange("usuario@test.com")
        viewModel.onNameChange("Usuario Test")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")

        viewModel.register()

        val state = viewModel.uiState.value

        assertNull("Email válido NO debe generar error", state.emailError)
        assertNull("Nombre válido NO debe generar error", state.nameError)
        assertNull("Password válida NO debe generar error", state.passwordError)
        assertNull("Confirmación válida NO debe generar error", state.confirmPasswordError)
    }
}
