package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.LoginViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginEmailInvalidoTest {

    @Test
    fun emailInvalidoMarcaError() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = LoginViewModel(app)

        viewModel.onEmailChange("correo-malo")
        viewModel.onPasswordChange("password123")

        viewModel.login()

        val state = viewModel.uiState.value

        assertNotNull("Debe haber error en email cuando el formato es inválido", state.emailError)
        assertFalse("No debería marcar login exitoso con email inválido", state.isLoginSuccessful)
    }
}
