package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.pi.supplybridge.models.Order

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
    var selectedTab by remember { mutableIntStateOf(0) }

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
                //2 -> ChatScreen()
                //3 -> AccountScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                val fetchedOrders = result.toOrdersList()
                orders = fetchedOrders
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao buscar pedidos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
                OrderItem(order)
            }
        }
    }
}

@Composable
fun OrdersScreen() {
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                val fetchedOrders = result.toOrdersList()
                orders = fetchedOrders
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao buscar pedidos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                val intent = Intent(context, NewOrderActivity()::class.java)
                context.startActivity(intent)
            },
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
                OrderItem(order)
            }
        }
    }
}

fun QuerySnapshot.toOrdersList(): List<Order> {
    val orders = mutableListOf<Order>()
    for (document in this) {
        val id = document.id
        val partName = document.getString("partName") ?: ""
        val storeName = document.getString("storeName") ?: ""
        val quantity = document.getString("quantity") ?: ""
        val paymentMethod = document.getString("paymentMethod") ?: ""
        val deliveryAddress = document.getString("deliveryAddress") ?: ""
        val notes = document.getString("notes") ?: ""
        orders.add(Order(id, partName, storeName, quantity, paymentMethod, deliveryAddress, notes))
    }
    return orders
}

@Composable
fun OrderItem(order: Order) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                val intent = Intent(context, OrderDetailActivity::class.java).apply {
                    putExtra("orderId", order.id)
                }
                context.startActivity(intent)
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
