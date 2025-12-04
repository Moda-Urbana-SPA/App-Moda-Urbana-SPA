package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.viewmodel.LoginViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginPasswordVaciaTest {

    @Test
    fun passwordVaciaMarcaError() {
        val app: Application = ApplicationProvider.getApplicationContext()
        val viewModel = LoginViewModel(app)

        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("")

        viewModel.login()

        val state = viewModel.uiState.value

        assertNotNull("Debe haber error en password cuando está vacía", state.passwordError)
        assertFalse("No debería marcar login exitoso con password vacía", state.isLoginSuccessful)
    }
}
