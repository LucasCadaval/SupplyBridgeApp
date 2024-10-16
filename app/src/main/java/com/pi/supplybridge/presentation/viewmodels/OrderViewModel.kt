package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.enums.OrderStatus
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.domain.usecases.order.*
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val saveOrderUseCase: SaveOrderUseCase,
    private val getOrdersByStatusUseCase: GetOrdersByStatusUseCase,
    private val getOrdersByUserIdUseCase: GetOrdersByUserIdUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val updateOrderWithSupplierUseCase: UpdateOrderWithSupplierUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _orderDetails = MutableStateFlow<Order?>(null)
    val orderDetails: StateFlow<Order?> = _orderDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _updateStatusResult = MutableStateFlow<Result<Boolean>>(Result.success(false))
    val updateStatusResult: StateFlow<Result<Boolean>> = _updateStatusResult

    private val _supplierName = MutableStateFlow<String?>(null)
    val supplierName: StateFlow<String?> = _supplierName

    private val tag = "OrderViewModel"

    fun loadOrders() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = getOrdersUseCase()
                _orders.value = result
            } catch (e: Exception) {
                Log.e(tag, "Failed to load orders", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrderById(orderId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = getOrderByIdUseCase(orderId)
                _orderDetails.value = result
                result?.supplierId?.let { loadUserName(it) }
            } catch (e: Exception) {
                Log.e(tag, "Failed to load order by ID", e)
                _orderDetails.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun saveOrder(order: Order): Boolean {
        return try {
            saveOrderUseCase(order)
            true
        } catch (e: Exception) {
            Log.e(tag, "Failed to save order", e)
            false
        }
    }

    fun loadOrdersByStatus(status: OrderStatus?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = if (status == null) {
                    getOrdersByStatusUseCase("Todos")
                } else {
                    getOrdersByStatusUseCase(status.name)
                }
                _orders.value = result
            } catch (e: Exception) {
                Log.e(tag, "Failed to load orders by status", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrdersByUser(userId: String, isStore: Boolean) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = getOrdersByUserIdUseCase(userId, isStore)
                _orders.value = result
            } catch (e: Exception) {
                Log.e(tag, "Failed to load orders by user", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = updateOrderStatusUseCase(orderId, newStatus)
                _updateStatusResult.value = Result.success(result)
                if (newStatus == OrderStatus.FINALIZED) {
                    updateOrderCompletionDate(orderId)
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to update order status", e)
                _updateStatusResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptBidAndSetSupplier(orderId: String, supplierId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = updateOrderWithSupplierUseCase.invoke(orderId,
                    supplierId,
                    OrderStatus.NEGOTIATION)
                if (result) {
                    loadOrderById(orderId)
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to set supplier for order", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadUserName(userId: String) {
        viewModelScope.launch {
            try {
                val user = getUserByIdUseCase(userId)
                _supplierName.value = user?.name
            } catch (e: Exception) {
                Log.e(tag, "Failed to load user name by ID", e)
                _supplierName.value = null
            }
        }
    }

    private fun updateOrderCompletionDate(orderId: String) {
        viewModelScope.launch {
            try {
                updateOrderStatusUseCase(orderId, OrderStatus.FINALIZED)
            } catch (e: Exception) {
                Log.e(tag, "Failed to update order completion date", e)
            }
        }
    }
}
