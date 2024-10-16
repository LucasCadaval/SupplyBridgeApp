package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.data.repositories.OrderRepository

class UpdateOrderStatusUseCase (private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String, newStatus: String): Boolean {
        return orderRepository.updateOrderStatus(orderId, newStatus)
    }
}