package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Dashboard()
                }
            }
        }
    }
}

@Composable
fun Dashboard() {
    var selectedTab by remember { mutableStateOf(0) }

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
                            contentDescription = "Histórico",
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text("Histórico") }
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
                0 -> HomeScreen()
                1 -> HistoryScreen()
                2 -> ChatScreen()
                3 -> AccountScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        var searchQuery by remember { mutableStateOf("") }

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar pedido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        val orders = listOf(
            Order("Peça A", "Loja X"),
            Order("Peça B", "Loja Y"),
            Order("Peça C", "Loja Z"),
            Order("Peça D", "Loja X"),
            Order("Peça E", "Loja Y")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orders) { order ->
                OrderItem(order)
            }
        }
    }
}

data class Order(val partName: String, val storeName: String)

@Composable
fun OrderItem(order: Order) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
//                val intent = Intent(context, OrderDetailActivity::class.java)
//                intent.putExtra("partName", order.partName)
//                intent.putExtra("storeName", order.storeName)
//                context.startActivity(intent)
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
fun HistoryScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Histórico Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ChatScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Chat Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun AccountScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Conta Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardActivityPreview() {
    SupplyBridgeTheme {
        Dashboard()
    }
}
