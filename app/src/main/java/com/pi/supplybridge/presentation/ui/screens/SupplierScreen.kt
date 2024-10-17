package com.pi.supplybridge.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.presentation.ui.components.OrderItem
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SupplierScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),

) {
    var searchQuery by remember { mutableStateOf("") }
    val orders by orderViewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar pedido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) { order ->
                OrderItem(order, navController, userViewModel = userViewModel)
            }
        }
    }
}