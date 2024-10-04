package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
                0 -> HomeScreen()
                1 -> OrdersScreen()
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

@Composable
fun OrdersScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                // Ação para criar um novo pedido
//                val intent = Intent(context, NewOrderActivity::class.java)
//                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Criar Novo Pedido")
        }

        Text(
            text = "Histórico de Pedidos Fechados",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val closedOrders = listOf(
            Order("Peça F", "Loja Z"),
            Order("Peça G", "Loja W"),
            Order("Peça H", "Loja X"),
            Order("Peça I", "Loja Y"),
            Order("Peça J", "Loja V")
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(closedOrders) { order ->
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
                // Abrir uma visão mais detalhada do pedido
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem do usuário em formato circular
        Image(
            painter = painterResource(id = R.drawable.henry), // Substituir pelo ID correto da imagem do usuário
            contentDescription = "Imagem do Usuário",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentScale = ContentScale.Crop
        )

        // Nome do usuário
        Text(
            text = "Nome do Usuário", // Substitua pelo nome real do usuário
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )

        // Opções de conta
        Card(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp)
                .clickable {
                    // Ação ao clicar no card
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Gerenciar Conta", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp)
                .clickable {
                    // Ação ao clicar no card
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Notificações", style = MaterialTheme.typography.bodyLarge)
            }
        }


        Card(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 16.dp)
                .clickable {
                    // Ação ao clicar no card
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Sair", style = MaterialTheme.typography.bodyLarge)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DashboardActivityPreview() {
    SupplyBridgeTheme {
        Dashboard()
    }
}
