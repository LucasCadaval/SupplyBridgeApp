package com.pi.supplybridge.utils

fun validateFields(partName: String, quantity: Int?, paymentMethod: String, deliveryAddress: String): Boolean {
    val paymentMethods = listOf("Dinheiro", "Pix", "Boleto", "Cartão de Crédito", "Cartão de Débito")
    return partName.isNotBlank() && (quantity != null && quantity > 0) &&
            paymentMethods.contains(paymentMethod) && deliveryAddress.isNotBlank()
}
