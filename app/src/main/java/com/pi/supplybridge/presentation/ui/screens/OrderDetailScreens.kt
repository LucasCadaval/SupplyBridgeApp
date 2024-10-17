package com.pi.supplybridge.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.enums.UserType
import com.pi.supplybridge.presentation.ui.components.DetailRow
import com.pi.supplybridge.presentation.ui.components.StatusBadge
import com.pi.supplybridge.presentation.ui.components.StoreActions
import com.pi.supplybridge.presentation.ui.components.SupplierActions
import com.pi.supplybridge.presentation.viewmodels.BidViewModel
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderDetailScreen(
    orderId: String,
    onBackClick: () -> Unit,
    orderViewModel: OrderViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    bidViewModel: BidViewModel = koinViewModel()
) {
    val order by orderViewModel.orderDetails.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val supplierName by orderViewModel.supplierName.collectAsState()
    val userInfo by userViewModel.userInfo.collectAsState()
    val bids by bidViewModel.bids.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUserInfo()
    }

    LaunchedEffect(orderId) {
        orderViewModel.loadOrderById(orderId)
        bidViewModel.loadBids(orderId)
    }

    LaunchedEffect(order) {
        order?.storeId?.let { userId ->
            userViewModel.loadUserNameById(userId)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        Column {
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        order?.let {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    DetailRow(label = "Peça:", value = it.partName)
                                    DetailRow(label = "Loja:", value = userInfo?.userName ?: "Desconhecido")
                                    DetailRow(label = "Quantidade:", value = it.quantity.toString())
                                    DetailRow(label = "Método de Pagamento:", value = it.paymentMethod ?: "Não especificado")
                                    DetailRow(label = "Endereço de Entrega:", value = it.deliveryAddress)
                                    DetailRow(label = "Notas:", value = it.notes ?: "Sem notas")
                                    Text(
                                        text = "Status:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    StatusBadge(status = it.status)

                                    supplierName?.let { name ->
                                        DetailRow(label = "Fornecedor:", value = name)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        when (userInfo?.userType) {
                            UserType.STORE -> StoreActions(
                                orderId = orderId,
                                orderStatus = order?.status ?: OrderStatus.OPEN,
                                bids = bids,
                                bidViewModel = bidViewModel,
                                orderViewModel = orderViewModel,
                                userViewModel = userViewModel
                            )
                            UserType.SUPPLIER -> SupplierActions(
                                order = order!!,
                                bidViewModel = bidViewModel,
                                userViewModel = userViewModel,
                                bids = bids
                            )
                            null -> Unit
                        }
                    }
                }
            }
        }
    }
}
