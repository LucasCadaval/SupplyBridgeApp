package com.pi.supplybridge.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.presentation.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

fun formatCNPJ(cnpj: String): String {
    return if (cnpj.length == 14) {
        "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${cnpj.substring(5, 8)}/${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
    } else {
        cnpj
    }
}

fun unformatCNPJ(cnpj: String): String {
    return cnpj.replace(Regex("[^\\d]"), "")
}

@Composable
fun ProfileScreen(
    userId: String,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(userId) {
        profileViewModel.loadUserData(userId)
    }

    val user by profileViewModel.user.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val isSaveSuccessful by profileViewModel.isSaveSuccessful.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    LaunchedEffect(isSaveSuccessful) {
        isSaveSuccessful?.let { success ->
            if (success) {
                Toast.makeText(context, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao salvar os dados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Exibir Toast com mensagem de erro, se houver
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (isLoading && user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        user?.let { currentUser ->
            var name by remember { mutableStateOf(currentUser.name) }
            var email by remember { mutableStateOf(currentUser.email) }
            var cnpj by remember { mutableStateOf(formatCNPJ(currentUser.cnpj)) }

            var isNameEditable by remember { mutableStateOf(false) }
            var isCnpjEditable by remember { mutableStateOf(false) }
            var isEmailEditable by remember { mutableStateOf(false) }

            // Layout Principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // Card Envolvendo os Campos de Entrada
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Campo Nome com Ícone
                        OutlinedTextField(
                            value = name,
                            onValueChange = { if (isNameEditable) name = it },
                            label = { Text("Nome") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Ícone Nome"
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { isNameEditable = !isNameEditable }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar Nome"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            enabled = isNameEditable,
                            singleLine = true
                        )

                        // Campo CNPJ com Ícone
                        OutlinedTextField(
                            value = cnpj,
                            onValueChange = { if (isCnpjEditable) cnpj = it },
                            label = { Text("CNPJ") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Face, // Ícone para CNPJ
                                    contentDescription = "Ícone CNPJ"
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { isCnpjEditable = !isCnpjEditable }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar CNPJ"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            enabled = isCnpjEditable,
                            singleLine = true
                        )

                        // Campo E-mail com Ícone
                        OutlinedTextField(
                            value = email,
                            onValueChange = { if (isEmailEditable) email = it },
                            label = { Text("E-mail") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Ícone E-mail"
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { isEmailEditable = !isEmailEditable }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar E-mail"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            enabled = isEmailEditable,
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão de Salvar Alterações Estilizado
                Button(
                    onClick = {
                        profileViewModel.updateUser(name, unformatCNPJ(cnpj), email) // Remove a formatação ao salvar
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Salvar Alterações")
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Usuário não encontrado")
            }
        }
    }
}