package com.pi.supplybridge.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pi.supplybridge.R
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.presentation.ui.navigation.Screen
import com.pi.supplybridge.presentation.viewmodels.OrderViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun DashboardScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showExitConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitConfirmationDialog = true // Mostra o diálogo de confirmação
    }

    if (showExitConfirmationDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                // Aqui você pode fazer o logout do usuário se necessário
                navController.popBackStack() // Volta para a tela anterior
            },
            onDismiss = {
                showExitConfirmationDialog = false // Fecha o diálogo
            }
        )
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = "Pedidos",
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text("Pedidos") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat),
                            contentDescription = "Chat",
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text("Chat") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_account),
                            contentDescription = "Conta",
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text("Conta") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardHomeScreen(navController)
                1 -> OrdersScreen(navController)
                //2 -> ChatScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

@Composable
fun DashboardHomeScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val orders by orderViewModel.orders.collectAsState()
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            }
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
                OrderItem(order, navController)
            }
        }
    }
}

@Composable
fun OrdersScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = koinViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.navigate(Screen.NewOrder.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Criar Novo Pedido")
        }

        Text(
            text = "Histórico de Pedidos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) { order ->
                OrderItem(order, navController)
            }
        }
    }
}

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
            }
        }
    }
}

@Composable
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sair")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Sair da conta") },
        text = { Text("Tem certeza que deseja sair?") },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}
