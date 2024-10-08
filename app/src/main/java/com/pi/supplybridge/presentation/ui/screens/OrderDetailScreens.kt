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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderDetailScreen(
    orderId: String,
    onBackClick: () -> Unit,
    orderViewModel: OrderViewModel = koinViewModel()
) {
    val order by orderViewModel.orderDetails.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val context = LocalContext.current

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
                            }
                        }
                    }
                }
            }
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
