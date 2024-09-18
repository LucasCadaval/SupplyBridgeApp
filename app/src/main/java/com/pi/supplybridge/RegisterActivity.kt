package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.caelum.stella.validation.CNPJValidator
import br.com.caelum.stella.validation.InvalidStateException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import com.pi.supplybridge.utils.ValidationUtils

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

fun saveUserDataToFirestore(uid: String?, name: String, cnpj: String, email: String) {
    val db = FirebaseFirestore.getInstance()

    val user = hashMapOf(
        "uid" to uid,
        "name" to name,
        "cnpj" to cnpj,
        "email" to email
    )

    uid?.let {
        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "dados de usuario salvos com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "erro ao salvar dados do usuário: ${e.message}", e)
            }
    }
}

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = cnpj,
            onValueChange = { cnpj = it },
            label = { Text("CNPJ") }
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") }
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
            when {
                name.isEmpty() -> {
                    Toast.makeText(context, "Preencha o nome", Toast.LENGTH_SHORT).show()
                }
                cnpj.isEmpty() || !ValidationUtils.isValidCNPJ(cnpj) -> {
                    Toast.makeText(context, "CNPJ inválido", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(context, "E-mail inválido", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(context, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Auth", "Usuário criado com sucesso: ${auth.currentUser?.uid}")
                                saveUserDataToFirestore(auth.currentUser?.uid, name, cnpj, email)
                                onLoginClick()
                            } else {
                                Log.e("Auth", "Erro ao fazer o registro: ${task.exception?.message}", task.exception)
                                Toast.makeText(context, "Erro ao registrar", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }) {
            Text("Registrar")
        }
    }
}