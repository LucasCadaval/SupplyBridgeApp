package com.pi.supplybridge.data.repositories

import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.models.Bid
import kotlinx.coroutines.tasks.await

class BidRepository(private val service: FirebaseService) {
    private val bidsCollection = service.firestoreInstance
        .collection("bids")

    suspend fun placeBid(bid: Bid): Boolean {
        return try {
            val bidRef = bidsCollection.document()
            bidRef.set(bid.copy(bidId = bidRef.id)).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getBidsByOrderId(orderId: String): List<Bid> {
        return try {
            val bidsSnapshot = bidsCollection
                .whereEqualTo("orderId", orderId)
                .get().await()
            val bidsList = bidsSnapshot.toObjects(Bid::class.java)
            bidsList
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateBidStatus(bidId: String, newStatus: String): Boolean {
        return try {
            bidsCollection.document(bidId)
                .update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
