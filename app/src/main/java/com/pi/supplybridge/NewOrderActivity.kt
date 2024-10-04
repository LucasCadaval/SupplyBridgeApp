package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.models.Order
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown

class NewOrderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NewOrderScreen()
                }
            }
        }
    }
}

@Composable
fun NewOrderScreen() {
    var partName by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val paymentMethods = listOf("Dinheiro", "Pix", "Boleto", "Cartão de Crédito", "Cartão de Débito")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    context.startActivity(Intent(context, DashboardActivity::class.java))
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Novo Pedido",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF3D3D3D)),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextField(
                        value = partName,
                        onValueChange = { partName = it },
                        label = { Text("Nome da Peça") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = { Text("Nome da Loja") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantidade") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = paymentMethod,
                            onValueChange = {},
                            label = { Text("Método de Pagamento") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expandir")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Gray,
                                focusedIndicatorColor = Color.Blue,
                                unfocusedIndicatorColor = Color.LightGray,
                                cursorColor = Color.Blue
                            )
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method) },
                                    onClick = {
                                        paymentMethod = method
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = { Text("Endereço de Entrega") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas Adicionais") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Gray,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.LightGray,
                            cursorColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (validateFields(partName, storeName, quantity, paymentMethod, deliveryAddress)) {
                                val orderData = hashMapOf(
                                    "partName" to partName,
                                    "storeName" to storeName,
                                    "quantity" to quantity,
                                    "paymentMethod" to paymentMethod,
                                    "deliveryAddress" to deliveryAddress,
                                    "notes" to notes
                                )

                                val db = FirebaseFirestore.getInstance()
                                db.collection("orders")
                                    .add(orderData)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                        context.startActivity(Intent(context, DashboardActivity::class.java)) // redireciona para Dashboard após salvar
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Salvar Pedido")
                    }
                }
            }
        }
    }
}

fun validateFields(partName: String, storeName: String, quantity: String, paymentMethod: String, deliveryAddress: String): Boolean {
    val paymentMethods = listOf("Dinheiro", "Pix", "Boleto", "Cartão de Crédito", "Cartão de Débito")
    return partName.isNotBlank() && storeName.isNotBlank() && quantity.isNotBlank() &&
            paymentMethods.contains(paymentMethod) && deliveryAddress.isNotBlank()
}

@Preview(showBackground = true)
@Composable
fun NewOrderScreenPreview() {
    SupplyBridgeTheme {
        NewOrderScreen()
    }
}
