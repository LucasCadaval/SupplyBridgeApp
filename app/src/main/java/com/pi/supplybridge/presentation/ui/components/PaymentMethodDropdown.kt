package com.pi.supplybridge.presentation.ui.components

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodDropdown(
    paymentMethod: String,
    onValueChange: (String) -> Unit,
    paymentMethods: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = paymentMethod,
            onValueChange = {},
            readOnly = true,
            label = { Text("Método de Pagamento") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Gray,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = Color.Blue
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            paymentMethods.forEach { method ->
                DropdownMenuItem(
                    text = { Text(method) },
                    onClick = {
                        onValueChange(method)
                        expanded = false
                    }
                )
            }
        }
    }
}
