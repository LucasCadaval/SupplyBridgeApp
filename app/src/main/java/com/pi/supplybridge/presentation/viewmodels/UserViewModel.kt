package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import com.pi.supplybridge.domain.usecases.user.SaveUserUseCase
import com.pi.supplybridge.domain.usecases.user.StorePreferencesUseCase
import com.pi.supplybridge.domain.usecases.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val storePreferencesUseCase: StorePreferencesUseCase
) : ViewModel() {
    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

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

    fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val info = storePreferencesUseCase.getUserInfo()
                _userInfo.value = info
                Log.d(tag, "Store info loaded: $info")
            } catch (e: Exception) {
                Log.e(tag, "Failed to load store info", e)
            }
        }
    }

    suspend fun getUserNameDirectly(userId: String): String? {
        return try {
            val user = getUserByIdUseCase(userId)
            user?.name
        } catch (e: Exception) {
            Log.e(tag, "Failed to load user name by ID", e)
            null
        }
    }

    fun loadUserNameById(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = getUserByIdUseCase(userId)
                if (user != null) {
                    _userInfo.value?.userName = user.name
                }
                Log.d(tag, "Store name loaded: ${user?.name}")
            } catch (e: Exception) {
                Log.e(tag, "Failed to load user name by ID", e)
                _userInfo.value?.userName = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
