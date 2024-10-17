package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.Bid
import com.pi.supplybridge.domain.usecases.bid.GetBidsByOrderUseCase
import com.pi.supplybridge.domain.usecases.bid.PlaceBidUseCase
import com.pi.supplybridge.domain.usecases.bid.UpdateBidStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BidViewModel(
    private val placeBidUseCase: PlaceBidUseCase,
    private val getBidsByOrderUseCase: GetBidsByOrderUseCase,
    private val updateBidStatusUseCase: UpdateBidStatusUseCase
) : ViewModel() {

    private val _bids = MutableStateFlow<List<Bid>>(emptyList())
    val bids: StateFlow<List<Bid>> = _bids

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadBids(orderId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("BID", "Iniciando carregamento de lances para o pedido: $orderId")
                val loadedBids = getBidsByOrderUseCase(orderId)
                Log.d("BID", "Lances carregados: $loadedBids")
                _bids.value = loadedBids
                Log.d("BID", "Bids StateFlow atualizado: ${_bids.value}")
            } catch (e: Exception) {
                Log.e("BID", "Erro ao carregar lances: ", e)
                _bids.value = emptyList()
            } finally {
                _isLoading.value = false
                Log.d("BID", "Estado de carregamento: ${_isLoading.value}")
            }
        }
    }

    fun placeBid(bid: Bid) {
        viewModelScope.launch {
            try {
                Log.d("BID", "Enviando lance: $bid")
                placeBidUseCase(bid)
                loadBids(bid.orderId)
            } catch (e: Exception) {
                Log.e("BID", "Erro ao enviar lance: ", e)
            }
        }
    }

    fun updateBidStatus(bidId: String, newStatus: String, orderId: String) {
        viewModelScope.launch {
            try {
                Log.d("BID", "att do lance $bidId para $newStatus")
                updateBidStatusUseCase(bidId, newStatus)
                loadBids(orderId)
            } catch (e: Exception) {
                Log.e("BI", "Erro ao atualizar status do lance: ", e)
            }
        }
    }
}
