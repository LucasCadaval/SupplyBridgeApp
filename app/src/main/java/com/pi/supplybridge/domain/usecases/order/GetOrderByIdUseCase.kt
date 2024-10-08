package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.data.repositories.OrderRepository
import com.pi.supplybridge.domain.models.Order

class GetOrderByIdUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(orderId: String): Order? {
        return orderRepository.getOrderById(orderId)
    }
}
