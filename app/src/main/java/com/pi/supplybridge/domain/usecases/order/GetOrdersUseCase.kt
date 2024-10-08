package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.data.repositories.OrderRepository

class GetOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(): List<Order> {
        return orderRepository.getOrders()
    }
}
