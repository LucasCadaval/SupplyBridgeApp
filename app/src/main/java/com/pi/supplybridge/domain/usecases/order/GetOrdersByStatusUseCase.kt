package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.data.repositories.OrderRepository

class GetOrdersByStatusUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(status: String): List<Order> {
        return orderRepository.getOrdersByStatus(status)
    }
}
