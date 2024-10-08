package com.pi.supplybridge.domain.models

data class Order(
    val id: String = "",
    val partName: String = "",
    val storeName: String = "",
    val quantity: String = "",
    val paymentMethod: String = "",
    val deliveryAddress: String = "",
    val notes: String = ""
)

