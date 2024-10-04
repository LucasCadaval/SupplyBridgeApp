package com.pi.supplybridge.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.pi.supplybridge.models.Order
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

class OrderDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val orderId = intent.getStringExtra("orderId") ?: ""
                    OrderDetailScreen(orderId)
                }
            }
        }
    }
}

@Composable
fun OrderDetailScreen(orderId: String) {
    var order by remember { mutableStateOf<Order?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document(orderId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    order = Order(
                        id = document.id,
                        partName = document.getString("partName") ?: "",
                        storeName = document.getString("storeName") ?: "",
                        quantity = document.getString("quantity") ?: "",
                        paymentMethod = document.getString("paymentMethod") ?: "",
                        deliveryAddress = document.getString("deliveryAddress") ?: "",
                        notes = document.getString("notes") ?: ""
                    )
                } else {
                    Toast.makeText(context, "Pedido não encontrado.", Toast.LENGTH_SHORT).show()
                }
                loading = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Erro ao buscar detalhes do pedido: ${e.message}", Toast.LENGTH_SHORT).show()
                loading = false
            }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, DashboardActivity::class.java))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Detalhes do Pedido",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF3D3D3D)),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                }

                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    order?.let {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                DetailRow(label = "Peça:", value = it.partName)
                                DetailRow(label = "Loja:", value = it.storeName)
                                DetailRow(label = "Quantidade:", value = it.quantity)
                                DetailRow(label = "Método de Pagamento:", value = it.paymentMethod)
                                DetailRow(label = "Endereço de Entrega:", value = it.deliveryAddress)
                                DetailRow(label = "Notas:", value = it.notes)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black, fontSize = MaterialTheme.typography.bodyMedium.fontSize)) {
                append("$label ")
            }
            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                append(value)
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderDetailScreenPreview() {
    SupplyBridgeTheme {
        OrderDetailScreen(orderId = "exemplo_id")
    }
}
