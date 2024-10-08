package com.pi.supplybridge.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.presentation.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val user by profileViewModel.user.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    user?.let { currentUser ->
        var name by remember { mutableStateOf(currentUser.name) }
        var email by remember { mutableStateOf(currentUser.email) }
        var cnpj by remember { mutableStateOf(currentUser.cnpj) }

        var isNameEditable by remember { mutableStateOf(false) }
        var isCnpjEditable by remember { mutableStateOf(false) }
        var isEmailEditable by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { if (isNameEditable) name = it },
                    label = { Text("Nome") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxWidth(0.85f),
                    enabled = isNameEditable
                )
                IconButton(onClick = { isNameEditable = !isNameEditable }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Nome")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = cnpj,
                    onValueChange = { if (isCnpjEditable) cnpj = it },
                    label = { Text("CNPJ") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxWidth(0.85f),
                    enabled = isCnpjEditable
                )
                IconButton(onClick = { isCnpjEditable = !isCnpjEditable }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar CNPJ")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { if (isEmailEditable) email = it },
                    label = { Text("E-mail") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .fillMaxWidth(0.85f),
                    enabled = isEmailEditable
                )
                IconButton(onClick = { isEmailEditable = !isEmailEditable }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar E-mail")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    profileViewModel.updateUser(name, cnpj, email)
                    Toast.makeText(context, "Dados atualizados", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Salvar Alterações")
                }
            }
        }
    }
}

