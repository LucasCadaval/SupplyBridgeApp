package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import com.pi.supplybridge.domain.usecases.user.SaveUserUseCase
import com.pi.supplybridge.domain.usecases.user.StorePreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val storePreferencesUseCase: StorePreferencesUseCase
) : ViewModel() {

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    private val _storeInfo = MutableStateFlow<Pair<String?, String?>>(null to null)
    val storeInfo: StateFlow<Pair<String?, String?>> = _storeInfo

    private val tag = "UserViewModel"

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSaveSuccessful = MutableStateFlow<Boolean?>(null)
    val isSaveSuccessful: StateFlow<Boolean?> = _isSaveSuccessful

    fun saveUser(user: User) {
        _isLoading.value = true
        viewModelScope.launch {
            val success = saveUserUseCase(user)
            _isSaveSuccessful.value = success
            _isLoading.value = false
        }
    }

    fun clearSaveState() {
        _isSaveSuccessful.value = null
    }

    fun loadUserType() {
        viewModelScope.launch {
            try {
                val type = storePreferencesUseCase.getUserType()
                _userType.value = type
                Log.d(tag, "User type loaded: $type")
            } catch (e: Exception) {
                Log.e(tag, "Failed to load user type", e)
            }
        }
    }

    fun loadStoreInfo() {
        viewModelScope.launch {
            try {
                val info = storePreferencesUseCase.getStoreInfo()
                _storeInfo.value = info
                Log.d(tag, "Store info loaded: $info")
            } catch (e: Exception) {
                Log.e(tag, "Failed to load store info", e)
            }
        }
    }

    suspend fun saveUserType(userType: String) {
        withContext(Dispatchers.IO) {
            storePreferencesUseCase.saveUserType(userType)
        }
    }

    suspend fun saveStoreInfo(storeId: String, storeName: String) {
        withContext(Dispatchers.IO) {
            storePreferencesUseCase.saveStoreInfo(storeId, storeName)
        }
    }
}
