package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.Order
import com.pi.supplybridge.domain.usecases.order.GetOrderByIdUseCase
import com.pi.supplybridge.domain.usecases.order.GetOrdersUseCase
import com.pi.supplybridge.domain.usecases.order.SaveOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val saveOrderUseCase: SaveOrderUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _orderDetails = MutableStateFlow<Order?>(null)
    val orderDetails: StateFlow<Order?> = _orderDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
            } catch (e: Exception) {
                _orderDetails.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun saveOrder(order: Order): Boolean {
        return try {
            saveOrderUseCase.invoke(order)
            true
        } catch (e: Exception) {
            false
        }
    }
}
