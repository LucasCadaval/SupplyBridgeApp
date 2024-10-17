package com.pi.supplybridge.presentation.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.ui.components.InputField
import com.pi.supplybridge.presentation.ui.components.NewOrderHeader
import com.pi.supplybridge.presentation.ui.components.PaymentMethodDropdown
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import com.pi.supplybridge.utils.validateFields
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewOrderScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    var partName by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var deliveryAddress by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val paymentMethods = listOf("Dinheiro", "Pix", "Boleto", "Cartão de Crédito", "Cartão de Débito")
    val coroutineScope = rememberCoroutineScope()
    val userInfo by userViewModel.userInfo.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("NewOrderScreen", "Loading user info...")
        userViewModel.loadUserInfo()
    }

    Surface(color = Color(0xFFEFEFEF)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            NewOrderHeader(onBackClick = {
                Log.d("NewOrderScreen", "Back button clicked")
                navController.popBackStack()
            })

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
                    InputField(
                        label = "Nome da Peça",
                        value = partName,
                        onValueChange = {
                            Log.d("NewOrderScreen", "Part Name updated: $it")
                            partName = it
                        }
                    )

                    InputField(
                        label = "Quantidade",
                        value = quantityText,
                        onValueChange = {
                            Log.d("NewOrderScreen", "Quantity updated: $it")
                            quantityText = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )

                    PaymentMethodDropdown(
                        paymentMethod = paymentMethod,
                        onValueChange = {
                            Log.d("NewOrderScreen", "Payment Method selected: $it")
                            paymentMethod = it
                        },
                        paymentMethods = paymentMethods
                    )

                    InputField(
                        label = "Endereço de Entrega",
                        value = deliveryAddress,
                        onValueChange = {
                            Log.d("NewOrderScreen", "Delivery Address updated: $it")
                            deliveryAddress = it
                        }
                    )

                    InputField(
                        label = "Notas Adicionais (opcional)",
                        value = notes,
                        onValueChange = {
                            Log.d("NewOrderScreen", "Notes updated: $it")
                            notes = it
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val quantity = quantityText.toIntOrNull()
                            Log.d("NewOrderScreen", "Quantity as Int: $quantity")

                            if (validateFields(partName, quantity, paymentMethod, deliveryAddress)) {
                                Log.d("NewOrderScreen", "All fields validated successfully")
                                coroutineScope.launch {
                                    loading = true
                                    try {
                                        Log.d("NewOrderScreen", "User info: $userInfo")

                                        if (userInfo?.userId == null) {
                                            Log.e("NewOrderScreen", "Error: userId is null")
                                        }

                                        val order = userInfo?.userId?.let {
                                            Order(
                                                partName = partName,
                                                storeId = it,
                                                quantity = quantity ?: 0,
                                                paymentMethod = paymentMethod,
                                                deliveryAddress = deliveryAddress,
                                                notes = notes,
                                                status = OrderStatus.OPEN
                                            )
                                        }

                                        Log.d("NewOrderScreen", "Order object created: $order")

                                        val success = order?.let { orderViewModel.saveOrder(it) } ?: false
                                        loading = false
                                        if (success) {
                                            Log.d("NewOrderScreen", "Order saved successfully")
                                            Toast.makeText(context, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        } else {
                                            Log.d("NewOrderScreen", "Failed to save order")
                                            Toast.makeText(context, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Log.e("NewOrderScreen", "Unexpected error saving order", e)
                                        Toast.makeText(context, "Erro inesperado ao salvar o pedido.", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        loading = false
                                    }
                                }
                            } else {
                                Log.d("NewOrderScreen", "Field validation failed")
                                Toast.makeText(context, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
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







