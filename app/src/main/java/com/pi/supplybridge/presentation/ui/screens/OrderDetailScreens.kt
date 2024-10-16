package com.pi.supplybridge.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderDetailScreen(
    orderId: String,
    onBackClick: () -> Unit,
    orderViewModel: OrderViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    val order by orderViewModel.orderDetails.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val userType by userViewModel.userType.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUserType()
    }

    LaunchedEffect(orderId) {
        orderViewModel.loadOrderById(orderId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        text = "Detalhes do Pedido",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF3D3D3D)),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    order?.let {
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
                                DetailRow(label = "Peça:", value = it.partName)
                                DetailRow(label = "Loja:", value = it.storeName)
                                DetailRow(label = "Quantidade:", value = it.quantity)
                                DetailRow(label = "Método de Pagamento:", value = it.paymentMethod)
                                DetailRow(label = "Endereço de Entrega:", value = it.deliveryAddress)
                                DetailRow(label = "Notas:", value = it.notes)
                                DetailRow(label = "Status:", value = it.status)

                                Spacer(modifier = Modifier.height(16.dp))

                                // Exibir ações baseadas no tipo de usuário
                                when (userType) {
                                    "store" -> StoreActions(
                                        onStatusChange = { newStatus ->
                                            orderViewModel.updateOrderStatus(orderId, newStatus)
                                        }
                                    )
                                    "supplier" -> SupplierActions(
                                        onLanceAceito = {
                                            //orderViewModel.acceptBid(orderId)
                                        },
                                        onLanceNegado = {
                                            //orderViewModel.rejectBid(orderId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoreActions(onStatusChange: (String) -> Unit) {
    Column {
        Text("Opções da Loja:", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onStatusChange("Em negociação") }, modifier = Modifier.fillMaxWidth()) {
            Text("Marcar como Em Negociação")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onStatusChange("Finalizada") }, modifier = Modifier.fillMaxWidth()) {
            Text("Marcar como Finalizada")
        }
    }
}

@Composable
fun SupplierActions(onLanceAceito: () -> Unit, onLanceNegado: () -> Unit) {
    Column {
        Text("Opções do Fornecedor:", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onLanceAceito, modifier = Modifier.fillMaxWidth()) {
            Text("Aceitar Lance - Tenho a peça")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onLanceNegado, modifier = Modifier.fillMaxWidth()) {
            Text("Negar Lance - Não Tenho a peça")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                append("$label ")
            }
            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                append(value)
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
}
