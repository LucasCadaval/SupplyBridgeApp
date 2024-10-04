package com.pi.supplybridge.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import com.pi.supplybridge.utils.ValidationUtils


class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getUserDataFromFirestore { user ->
            setContent {
                SupplyBridgeTheme {
                    ProfileScreen(user = user) { updatedUser ->
                        saveUserDataToFirestore(
                            updatedUser.uid,
                            updatedUser.name,
                            updatedUser.cnpj,
                            updatedUser.email
                        )
                        finish()
                    }
                }
            }
        }
    }

    private fun getUserDataFromFirestore(onUserDataReceived: (User) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = User(
                            uid = userId,
                            name = document.getString("name") ?: "",
                            email = document.getString("email") ?: "",
                            cnpj = document.getString("cnpj") ?: ""
                        )
                        onUserDataReceived(user)
                    } else {
                        Log.d("Firestore", "Documento não encontrado")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Erro ao buscar os dados: ${e.message}")
                }
        }
    }

    private fun saveUserDataToFirestore(uid: String, name: String?, cnpj: String?, email: String?) {
        val db = FirebaseFirestore.getInstance()
        val updates = hashMapOf<String, Any>()

        name?.let { updates["name"] = it }
        cnpj?.let { updates["cnpj"] = it }
        email?.let { updates["email"] = it }

        db.collection("users").document(uid)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Dados do usuário atualizados com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao atualizar dados: ${e.message}")
            }
    }
}

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val cnpj: String
)

@Composable
fun ProfileScreen(
    user: User,
    onSaveClick: (User) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var cnpj by remember { mutableStateOf(user.cnpj) }
    var password by remember { mutableStateOf("") }

    var isNameEditable by remember { mutableStateOf(false) }
    var isCnpjEditable by remember { mutableStateOf(false) }
    var isEmailEditable by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                val updatedUser = user.copy(
                    name = if (isNameEditable) name else user.name,
                    cnpj = if (isCnpjEditable) cnpj else user.cnpj,
                    email = if (isEmailEditable) email else user.email
                )

                if (isValidUserData(updatedUser)) {
                    onSaveClick(updatedUser)
                } else {
                    Toast.makeText(context, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("Salvar Alterações")
        }
    }
}

fun isValidUserData(user: User): Boolean {
    return (user.name.isNotEmpty() &&
            ValidationUtils.isValidCNPJ(user.cnpj) &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches())
}