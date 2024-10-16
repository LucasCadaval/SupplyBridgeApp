package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.data.repositories.OrderRepository

class GetOrdersByUserIdUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(userId: String, isStore: Boolean): List<Order> {
        return orderRepository.getOrdersByUserId(userId, isStore)
    }
}
