package com.pi.supplybridge.data.repositories

import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.models.Order
import kotlinx.coroutines.tasks.await

class OrderRepository(private val service: FirebaseService) {
    private val ordersCollection = service.firestoreInstance
        .collection("orders")

    suspend fun getOrders(): List<Order> {
        return try {
            val snapshot = ordersCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                val order = document.toObject(Order::class.java)
                order?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getOrderById(orderId: String): Order? {
        return try {
            val document = ordersCollection.document(orderId).get().await()
            document.toObject(Order::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveOrder(order: Order) {
        ordersCollection.add(order).await()
    }
}
