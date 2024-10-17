package com.pi.supplybridge.domain.models

import com.pi.supplybridge.domain.enums.OrderStatus
import java.util.Date

data class Order(
    val id: String = "",
    val partName: String = "",
    val storeId: String = "",
    val supplierId: String? = null,
    val quantity: Int = 1,
    val paymentMethod: String? = null,
    val deliveryAddress: String = "",
    val notes: String? = null,
    val status: OrderStatus = OrderStatus.OPEN,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null,
    val completedAt: Date? = null
)
