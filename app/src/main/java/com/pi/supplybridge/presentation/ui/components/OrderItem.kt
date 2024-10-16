package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.ui.navigation.Screen

@Composable
fun OrderItem(order: Order, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate(Screen.OrderDetail.createRoute(order.id))
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Peça: ${order.partName}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Loja: ${order.storeName}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
