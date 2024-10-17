package com.pi.supplybridge.domain.usecases.bid

import com.pi.supplybridge.data.repositories.BidRepository

class UpdateBidStatusUseCase(private val bidRepository: BidRepository) {
    suspend operator fun invoke(bidId: String, status: String): Boolean {
        return bidRepository.updateBidStatus(bidId, status)
    }
}