package com.pi.supplybridge.domain.usecases.bid

import com.pi.supplybridge.data.repositories.BidRepository
import com.pi.supplybridge.domain.models.Bid

class PlaceBidUseCase(private val bidRepository: BidRepository) {
    suspend operator fun invoke(bid: Bid): Boolean {
        return bidRepository.placeBid(bid)
    }
}