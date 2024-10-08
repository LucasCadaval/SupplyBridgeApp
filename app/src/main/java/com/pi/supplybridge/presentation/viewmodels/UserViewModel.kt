package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import com.pi.supplybridge.domain.usecases.user.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _userDetails = MutableStateFlow<User?>(null)
    val userDetails: StateFlow<User?> = _userDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSaveSuccessful = MutableStateFlow<Boolean?>(null)
    val isSaveSuccessful: StateFlow<Boolean?> = _isSaveSuccessful

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val tag = "UserViewModel"

    fun loadUserById(userId: String) {
        _isLoading.value = true
        Log.d(tag, "Carregando usuário com ID: $userId")
        viewModelScope.launch {
            try {
                val result = getUserByIdUseCase(userId)
                _userDetails.value = result
                Log.d(tag, "Usuário carregado com sucesso: $result")
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar usuário: ${e.message}"
                Log.e(tag, "Erro ao carregar usuário: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveUser(user: User) {
        _isLoading.value = true
        Log.d(tag, "Salvando usuário: $user")
        viewModelScope.launch {
            try {
                val isSuccessful = saveUserUseCase(user)
                _isSaveSuccessful.value = isSuccessful
                if (!isSuccessful) {
                    _errorMessage.value = "Erro ao salvar dados do usuário"
                    Log.e(tag, "Erro ao salvar dados do usuário")
                } else {
                    Log.d(tag, "Usuário salvo com sucesso.")
                }
            } catch (e: Exception) {
                _isSaveSuccessful.value = false
                _errorMessage.value = "Erro ao salvar usuário: ${e.message}"
                Log.e(tag, "Erro ao salvar usuário: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
