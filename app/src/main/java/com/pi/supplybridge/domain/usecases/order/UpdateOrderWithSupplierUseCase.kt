package com.pi.supplybridge.domain.usecases.order

import com.pi.supplybridge.data.repositories.OrderRepository
import com.pi.supplybridge.domain.enums.OrderStatus

class UpdateOrderWithSupplierUseCase (private val orderRepository: OrderRepository) {
        suspend operator fun invoke(orderId: String, supplierId: String,
                                    newStatus: OrderStatus): Boolean {
        return orderRepository.updateOrderWithSupplier(orderId, supplierId,
            newStatus)
    }
}