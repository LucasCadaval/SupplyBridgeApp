package com.pi.supplybridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pi.supplybridge.presentation.ui.navigation.NavigationHost
import com.pi.supplybridge.presentation.ui.theme.SupplyBridgeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupplyBridgeTheme {
                val navController = rememberNavController()
                NavigationHost(navController = navController, modifier = Modifier)
            }
        }
    }
}
