package com.example.modaurbanaprototipoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.modaurbanaprototipoapp.ui.screens.ProfileScreen
import com.example.modaurbanaprototipoapp.ui.theme.ModaUrbanaPrototipoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModaUrbanaPrototipoAppTheme {
                ProfileScreen()
            }
        }
    }
}
