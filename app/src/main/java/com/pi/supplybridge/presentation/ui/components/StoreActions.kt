package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.models.Bid
import com.pi.supplybridge.presentation.viewmodels.BidViewModel
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel

@Composable
fun StoreActions(
    orderId: String,
    orderStatus: OrderStatus,
    bids: List<Bid>,
    bidViewModel: BidViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel
) {
    val isLoading by bidViewModel.isLoading.collectAsState()
    val acceptedBid = bids.find { it.status == "accepted" }
    val supplierNames = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(bids) {
        bids.forEach { bid ->
            if (!supplierNames.contains(bid.supplierId)) {
                val name = userViewModel.getUserNameDirectly(bid.supplierId)
                if (name != null) {
                    supplierNames[bid.supplierId] = name
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Lances Recebidos",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (acceptedBid != null) {
                Text(
                    text = "Lance Aceito",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val supplierName = supplierNames[acceptedBid.supplierId]
                if (supplierName != null) {
                    BidCard(bid = acceptedBid, showActions = false, supplierName = supplierName)
                }
            } else {
                bids.forEach { bid ->
                    val supplierName = supplierNames[bid.supplierId]
                    if (supplierName != null) {
                        BidCard(
                            bid = bid,
                            showActions = orderStatus == OrderStatus.OPEN || orderStatus == OrderStatus.NEGOTIATION,
                            supplierName = supplierName,
                            onAcceptClick = {
                                orderViewModel.acceptBidAndSetSupplier(orderId, bid.supplierId)
                                bidViewModel.updateBidStatus(bid.bidId, "accepted", orderId)
                            },
                            onRejectClick = {
                                bidViewModel.updateBidStatus(bid.bidId, "rejected", orderId)
                            }
                        )
                    }
                }
            }

            if (orderStatus == OrderStatus.OPEN || orderStatus == OrderStatus.NEGOTIATION) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { orderViewModel.updateOrderStatus(orderId, OrderStatus.FINALIZED) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Finalizar Pedido")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { orderViewModel.updateOrderStatus(orderId, OrderStatus.CANCELLED) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar Pedido")
                    }
                }
            }
        }
    }
}
