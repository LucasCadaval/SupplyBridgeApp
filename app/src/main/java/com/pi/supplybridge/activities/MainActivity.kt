package com.pi.supplybridge.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        onRegisterClick = { navigateToRegister() },
                        onLoginClick = { navigateToHome() },
                        onForgotPasswordClick = { navigateToForgotPassword() }
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

    private fun navigateToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val context = LocalContext.current
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
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Esqueceu a senha?",
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .clickable { onForgotPasswordClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val tipoUsuario = document.getString("userType")
                                        if (tipoUsuario == "fornecedor") {
                                            val intent = Intent(context, HomeActivity::class.java)
                                            context.startActivity(intent)
                                        } else if (tipoUsuario == "loja") {
                                            val intent = Intent(context, DashboardActivity::class.java)
                                            context.startActivity(intent)
                                        }
                                    } else {
                                        Toast.makeText(context, "Erro ao buscar informações do usuário", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao buscar dados do usuário", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(context, "Erro ao fazer o login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.size(16.dp))

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
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SupplyBridgeTheme {
        LoginScreen(
            onRegisterClick = {},
            onLoginClick = {},
            onForgotPasswordClick = {}
        )
    }
}
