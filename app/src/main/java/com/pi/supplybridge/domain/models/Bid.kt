package com.pi.supplybridge.domain.models

data class Bid(
    val bidId: String = "",
    val orderId: String = "",
    val supplierId: String = "",
    val storeId: String = "",
    val amount: Double = 0.0,
    val status: String = "pending"
)
