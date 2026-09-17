package com.danilo.conductorexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danilo.conductorexample.ui.catalog.GamesCatalogScreen
import com.danilo.conductorexample.ui.theme.ConductorExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConductorExampleTheme {
                GamesCatalogScreen(
                    onAddGameClick = {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.catalog_add_game_coming_soon),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}