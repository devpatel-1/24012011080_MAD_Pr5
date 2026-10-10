package com.dev.a24012011080_mad_pr5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuniTheme {
                var onLogin by rememberSaveable { mutableStateOf(true) }
                if (onLogin) LoginScreen(onSignUp = { onLogin = false })
                else RegisterScreen(onLogin = { onLogin = true })
            }
        }
    }
}