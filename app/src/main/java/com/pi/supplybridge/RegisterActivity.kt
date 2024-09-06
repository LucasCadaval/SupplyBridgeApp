package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RegisterScreen(onLoginClick = { navigateToLogin() })
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var cnpj by remember { mutableStateOf("") }
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("username") }
//        )

        Spacer(modifier = Modifier.size(16.dp))

//        OutlinedTextField(
//            value = cnpj,
//            onValueChange = { cnpj = it },
//            label = { Text("cnpj") }
//        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Já possui uma conta? Entre",
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .clickable { onLoginClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onLoginClick()
                    } else {
                        println("Erro ao fazer o registro")
                    }
                }
        }) {
            Text("Register")
        }
    }
}
