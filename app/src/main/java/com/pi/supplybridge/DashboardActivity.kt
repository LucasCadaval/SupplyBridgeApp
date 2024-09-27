package com.pi.supplybridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.pi.supplybridge.ui.theme.SupplyBridgeTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupplyBridgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Dashboard(name = "Lucas", email = "lucas@gmail.com")
                }
            }
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun Dashboard(name: String, email: String) {
    val stores = listOf(
        "Loja A" to "Peça 1",
        "Loja B" to "Peça 2",
        "Loja C" to "Peça 3",
        "Loja D" to "Peça 4",
        "Loja E" to "Peça 5"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (header, list, title) = createRefs()

            // Cabeçalho
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(color = Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Moldura redonda para a imagem do usuário
                    Image(
                        painter = painterResource(id = R.drawable.henry),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Texto com nome e email
                    Column {
                        Text(
                            text = "Olá, $name!",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }

            Text(
                text = "Pedidos em andamento:",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(title) {
                        top.linkTo(header.bottom)
                        start.linkTo(parent.start)
                    }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(list) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(16.dp)
            ) {
                items(stores) { supplier ->
                    SupplierItem(storeName = supplier.first, partName = supplier.second)
                    Spacer(modifier = Modifier.height(8.dp)) // Espaço entre os itens
                }
            }
        }
    }
}

@Composable
fun SupplierItem(storeName: String, partName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                println("Clicou em $storeName - $partName")
            }
            .padding(16.dp)
    ) {
        Column {
            Text(text = storeName, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Peça: $partName", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}






@Preview(showBackground = true)
@Composable
fun DashboardActivityPreview() {
    SupplyBridgeTheme {
        Dashboard(name = "lucas", email = "email@email.com")
    }
}
