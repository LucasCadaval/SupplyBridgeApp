package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Conteúdo da HomeActivity
                    Box(
                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
                    )
                    Button(onClick = {
                      val intent = Intent(this, ProfileActivity::class.java)
                       startActivity(intent)
                    })
                    {
                        Text(text = "Bem-vindo à Home!")
                    }
                }
            }
        }
    }
}
