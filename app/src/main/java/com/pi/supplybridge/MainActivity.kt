package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onRegisterClick = { navigateToRegister() },
                        onLoginClick = { navigateToHome() }
                    )
                }
            }
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name)
        )

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

        // Texto clicável para registro
        Text(
            text = "Não possui uma conta? Registre-se",
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoginClick()
                } else {
                    println("Erro ao fazer o login")
                }
            } }) {
            Text("Entrar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupplyBridgeTheme {
        LoginScreen(
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
