package com.pi.supplybridge.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.R
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import com.pi.supplybridge.utils.MaskVisualTransformation
import com.pi.supplybridge.utils.ValidationUtils

import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    userViewModel: UserViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val isLoading by userViewModel.isLoading.collectAsState()
    val isSaveSuccessful by userViewModel.isSaveSuccessful.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("fornecedor") }

    LaunchedEffect(isSaveSuccessful) {
        if (isSaveSuccessful == true) {
            Toast.makeText(context, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
            onLoginClick()
        } else if (isSaveSuccessful == false) {
            Toast.makeText(context, "Erro ao registrar o usuário.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(140.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name)
        )

        Spacer(modifier = Modifier.size(32.dp))

        Text(
            text = "Cadastre-se aqui!",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome da Empresa") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.size(16.dp))

        CNPJInputField(cnpj = cnpj, onCNPJChange = { cnpj = it })

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = "Qual é o seu papel?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(
                    selected = userType == "fornecedor",
                    onClick = { userType = "fornecedor" }
                )
                Text("Fornecedor")
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RadioButton(
                    selected = userType == "loja",
                    onClick = { userType = "loja" }
                )
                Text("Lojista")
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        Text(
            text = "Já possui uma conta? Entre aqui.",
            style = TextStyle(
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .clickable { onLoginClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = {
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
                        val user = User(
                            name = name,
                            userType = userType,
                            cnpj = cnpj,
                            email = email,
                            password = password
                        )

                        userViewModel.saveUser(user)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Registrar")
            }
        }
    }
}


@Composable
fun CNPJInputField(cnpj: String, onCNPJChange: (String) -> Unit) {
    var textState by remember { mutableStateOf(cnpj) }
    val cnpjMask = "##.###.###/####-##"
    val visualTransformation = MaskVisualTransformation(cnpjMask)

    OutlinedTextField(
        value = textState,
        onValueChange = { newValue ->
            if (newValue.length <= 14 && newValue.all { it.isDigit() }) {
                textState = newValue
                onCNPJChange(newValue)
            }
        },
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text("CNPJ") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.White
        )
    )
}
