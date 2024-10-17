package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.data.repositories.OrderRepository
import com.pi.supplybridge.domain.enums.OrderStatus

class UpdateOrderStatusUseCase (private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String, newStatus: OrderStatus): Boolean {
        return orderRepository.updateOrderStatus(orderId, newStatus)
    }
}