package com.example.modaurbanaprototipoapp

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.modaurbanaprototipoapp.data.local.SessionManager
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TokenGuardadoCorrectamenteTest {

    @Test
    fun tokenSeGuardaYSeLeeCorrectamente() = runTest {
        val app: Application = ApplicationProvider.getApplicationContext()
        val sessionManager = SessionManager(app)

        sessionManager.clearAuthToken()

        val tokenEsperado = "TOKEN_TEST_123"

        sessionManager.saveAuthToken(tokenEsperado)

        val tokenAlmacenado = sessionManager.getAuthToken()

        assertEquals(
            "El token guardado debe coincidir con el token leído",
            tokenEsperado,
            tokenAlmacenado
        )
    }
}
