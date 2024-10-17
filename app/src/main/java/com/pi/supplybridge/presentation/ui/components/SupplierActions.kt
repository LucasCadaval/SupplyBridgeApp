package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.models.Bid
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.viewmodels.BidViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun SupplierActions(
    order: Order,
    bids: List<Bid>,
    bidViewModel: BidViewModel,
    userViewModel: UserViewModel
) {
    var bidAmount by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val userInfo by userViewModel.userInfo.collectAsState()
    val acceptedBid = bids.find { it.status == "accepted" }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column {
        Text("Enviar um Lance", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (order.status == OrderStatus.OPEN || order.status == OrderStatus.NEGOTIATION) {
            OutlinedTextField(
                value = bidAmount,
                onValueChange = { bidAmount = it },
                label = { Text("Valor do Lance") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val bid = Bid(
                                orderId = order.id,
                                supplierId = userInfo?.userId ?: "",
                                storeId = order.storeId,
                                amount = bidAmount.toDouble()
                            )
                            bidViewModel.placeBid(bid)
                            Toast.makeText(context, "Lance enviado com sucesso!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Erro ao enviar o lance.", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Enviar Lance")
                }
            }
        } else if (acceptedBid != null) {
            Text(
                text = "Lance Aceito: R$${acceptedBid.amount}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Text(
                text = "Nenhum lance aceito para este pedido.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

