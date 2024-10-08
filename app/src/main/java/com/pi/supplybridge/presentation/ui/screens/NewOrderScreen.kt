package com.pi.supplybridge.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun NewOrderScreen(navController: NavController, orderViewModel: OrderViewModel = koinViewModel()) {
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
    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Surface(color = Color(0xFFEFEFEF)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                },
            verticalArrangement = Arrangement.Top
        ) {
            Header(onBackClick = { navController.popBackStack() })

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crie um novo pedido preenchendo os campos abaixo.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF3D3D3D)),
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputField(label = "Nome da Peça", value = partName, onValueChange = { partName = it })
                    InputField(label = "Nome da Loja", value = storeName, onValueChange = { storeName = it })
                    InputField(label = "Quantidade", value = quantity, onValueChange = { quantity = it })

                    PaymentMethodDropdown(
                        paymentMethod = paymentMethod,
                        onValueChange = { paymentMethod = it },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        paymentMethods = paymentMethods
                    )

                    InputField(label = "Endereço de Entrega", value = deliveryAddress, onValueChange = { deliveryAddress = it })
                    InputField(label = "Notas Adicionais (opcional)", value = notes, onValueChange = { notes = it })

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (validateFields(partName, storeName, quantity, paymentMethod, deliveryAddress)) {
                                coroutineScope.launch {
                                    loading = true
                                    val order = Order(
                                        partName = partName,
                                        storeName = storeName,
                                        quantity = quantity,
                                        paymentMethod = paymentMethod,
                                        deliveryAddress = deliveryAddress,
                                        notes = notes
                                    )
                                    val success = orderViewModel.saveOrder(
                                        order
                                    )
                                    loading = false
                                    if (success) {
                                        Toast.makeText(context, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = "Salvar Pedido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
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
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.LightGray,
            cursorColor = Color.Blue
        )
    )
}

@Composable
fun PaymentMethodDropdown(
    paymentMethod: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    paymentMethods: List<String>
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        TextField(
            value = paymentMethod,
            onValueChange = { /* No-op */ },
            label = { Text("Método de Pagamento") },
            trailingIcon = {
                IconButton(onClick = { onExpandedChange(true) }) {
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
            onDismissRequest = { onExpandedChange(false) }
        ) {
            paymentMethods.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method) },
                    onClick = {
                        onValueChange(method)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

fun validateFields(partName: String, storeName: String, quantity: String, paymentMethod: String, deliveryAddress: String): Boolean {
    val paymentMethods = listOf("Dinheiro", "Pix", "Boleto", "Cartão de Crédito", "Cartão de Débito")
    return partName.isNotBlank() && storeName.isNotBlank() && quantity.isNotBlank() &&
            paymentMethods.contains(paymentMethod) && deliveryAddress.isNotBlank()
}
