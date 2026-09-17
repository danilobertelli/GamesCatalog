package com.danilo.conductorexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danilo.conductorexample.ui.navigation.AppNavHost
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConductorExampleTheme {
                AppNavHost()
            }
        }
    }
}