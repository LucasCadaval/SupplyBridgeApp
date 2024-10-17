package com.pi.supplybridge.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import com.pi.supplybridge.utils.MaskVisualTransformation

@Composable
fun CNPJInputField(cnpj: String, onCNPJChange: (String) -> Unit) {
    var textState by remember { mutableStateOf(cnpj) }
    val cnpjMask = "##.###.###/####-##"
    val visualTransformation = MaskVisualTransformation(cnpjMask)

    OutlinedTextField(
        value = textState,
        onValueChange = { newValue ->
            if (newValue.length <= 14 && newValue.all { it.isDigit() }) {
                textState = newValue
                onCNPJChange(newValue)
            }
        },
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text("CNPJ") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.White
        )
    )
}