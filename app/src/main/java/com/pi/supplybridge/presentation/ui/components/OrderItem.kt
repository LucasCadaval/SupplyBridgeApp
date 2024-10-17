package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.ui.navigation.Screen
import com.pi.supplybridge.presentation.viewmodels.UserViewModel

@Composable
fun OrderItem(
    order: Order,
    navController: NavController,
    userViewModel: UserViewModel
) {
    var userName by remember { mutableStateOf<String>("Carregando...") }

    LaunchedEffect(order.storeId) {
        userViewModel.loadUserNameById(order.storeId)
    }

    val observedUserInfo by userViewModel.userInfo.collectAsState()

    LaunchedEffect(observedUserInfo) {
        userName = observedUserInfo?.userName ?: "Desconhecido"
    }

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
                Text(
                    text = "Peça: ${order.partName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Loja: $userName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusBadge(status = order.status)
            }
        }
    }
}
