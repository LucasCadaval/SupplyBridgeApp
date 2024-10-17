package com.pi.supplybridge.data.repositories

import android.util.Log
import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.models.Order
import kotlinx.coroutines.tasks.await
import java.util.Date

class OrderRepository(private val service: FirebaseService) {
    private val ordersCollection = service.firestoreInstance
        .collection("orders")

    suspend fun getOrders(): List<Order> {
        return try {
            Log.d("getOrders", "Attempting to fetch orders collection...")

            val snapshot = ordersCollection.get().await()
            Log.d("getOrders", "Fetched ${snapshot.documents.size} documents from orders collection.")

            snapshot.documents.mapNotNull { document ->
                val order = document.toObject(Order::class.java)
                if (order != null) {
                    Log.d("getOrders", "Order retrieved: ${order.copy(id = document.id)}")
                } else {
                    Log.e("getOrders", "Failed to convert document to Order: ${document.id}")
                }
                order?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("getOrders", "Error fetching orders: ${e.localizedMessage}", e)
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
            Log.d("OrderRepository", "Tentando salvar o pedido: ${order.toMap()}")
            ordersCollection
                .add(order.toMap())
                .await()
            Log.d("OrderRepository", "Pedido salvo com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("OrderRepository", "Erro ao salvar o pedido: ${e.localizedMessage}")
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
        val field = if (isStore) "storeId" else "supplierId"
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

    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Boolean {
        return try {
            ordersCollection.document(orderId)
                .update("status", newStatus, "updatedAt", Date())
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateOrderWithSupplier(orderId: String, supplierId: String,
                                        newStatus: OrderStatus? = null): Boolean {
        val updates = mutableMapOf<String, Any?>(
            "supplierId" to supplierId,
            "updatedAt" to Date()
        )
        newStatus?.let {
            updates["status"] = it
            if (it == OrderStatus.FINALIZED) updates["completedAt"] = Date()
        }

        return try {
            ordersCollection.document(orderId)
                .update(updates)
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
            "storeId" to storeId,
            "supplierId" to supplierId,
            "quantity" to quantity,
            "paymentMethod" to paymentMethod,
            "deliveryAddress" to deliveryAddress,
            "notes" to notes,
            "status" to status,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "completedAt" to completedAt
        )
    }
}
