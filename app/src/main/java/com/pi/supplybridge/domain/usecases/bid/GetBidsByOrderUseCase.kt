package com.pi.supplybridge.domain.usecases.bid

import com.pi.supplybridge.data.repositories.BidRepository
import com.pi.supplybridge.domain.models.Bid

class GetBidsByOrderUseCase(private val bidRepository: BidRepository) {
    suspend operator fun invoke(orderId: String): List<Bid> {
        return bidRepository.getBidsByOrderId(orderId)
    }
}
