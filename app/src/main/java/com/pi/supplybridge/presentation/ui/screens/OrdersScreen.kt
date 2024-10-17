package com.pi.supplybridge.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.enums.UserType
import com.pi.supplybridge.presentation.ui.components.OrderItem
import com.pi.supplybridge.presentation.ui.components.StatusFilter
import com.pi.supplybridge.presentation.ui.navigation.Screen
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import com.pi.supplybridge.presentation.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val userInfo by userViewModel.userInfo.collectAsState()
    var selectedStatus by remember { mutableStateOf<OrderStatus?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
        userViewModel.loadUserInfo()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Histórico de Pedidos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            StatusFilter(selectedStatus) { status ->
                selectedStatus = status
                coroutineScope.launch {
                    orderViewModel.loadOrdersByStatus(status)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(orders) { order ->
                        OrderItem(order, navController, userViewModel)
                    }
                }
            }
        }

        if (userInfo?.userType != UserType.SUPPLIER) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.NewOrder.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Criar Novo Pedido")
            }
        }
    }
}

