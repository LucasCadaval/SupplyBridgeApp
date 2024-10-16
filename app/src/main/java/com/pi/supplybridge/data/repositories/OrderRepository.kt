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

    suspend fun saveOrder(order: Order): Boolean {
        return try {
            ordersCollection
                .add(order.toMap())
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getOrdersByStatus(status: String): List<Order> {
        return try {
            val snapshot = ordersCollection
                .whereEqualTo("status", status)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Order::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getOrdersByUserId(userId: String, isStore: Boolean): List<Order> {
        val field = if (isStore) "storeName" else "supplierId"
        return try {
            val snapshot = ordersCollection
                .whereEqualTo(field, userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Order::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String): Boolean {
        return try {
            ordersCollection.document(orderId)
                .update("status", newStatus)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun Order.toMap(): Map<String, Any?> {
        return mapOf(
            "partName" to partName,
            "storeName" to storeName,
            "quantity" to quantity,
            "paymentMethod" to paymentMethod,
            "deliveryAddress" to deliveryAddress,
            "notes" to notes,
            "status" to status,
            "createdAt" to createdAt
        )
    }
}
