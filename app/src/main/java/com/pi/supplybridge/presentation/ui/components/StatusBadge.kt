package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pi.supplybridge.domain.enums.OrderStatus

@Composable
fun StatusBadge(status: OrderStatus) {
    val backgroundColor = when (status) {
        OrderStatus.OPEN -> Color.Green.copy(alpha = 0.2f)
        OrderStatus.NEGOTIATION -> Color.Yellow.copy(alpha = 0.2f)
        OrderStatus.FINALIZED -> Color.Blue.copy(alpha = 0.2f)
        OrderStatus.CANCELLED -> Color.Red.copy(alpha = 0.2f)
    }

    val textColor = when (status) {
        OrderStatus.OPEN -> Color.White
        OrderStatus.NEGOTIATION -> Color.White
        OrderStatus.FINALIZED -> Color.White
        OrderStatus.CANCELLED -> Color.White
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.description,
            color = textColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
