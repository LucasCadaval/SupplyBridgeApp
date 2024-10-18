package com.pi.supplybridge.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.domain.usecases.user.GetUserByIdUseCase
import com.pi.supplybridge.domain.usecases.user.UpdateUserUseCase
import com.pi.supplybridge.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSaveSuccessful = MutableStateFlow<Boolean?>(null)
    val isSaveSuccessful: StateFlow<Boolean?> = _isSaveSuccessful

    fun loadUserData(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedUser = getUserByIdUseCase(userId)
                _user.value = fetchedUser
            } catch (e: Exception) {
                // fazer
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(name: String, cnpj: String, email: String) {
        val currentUser = _user.value
        if (currentUser != null) {
            val updatedUser = currentUser.copy(name = name, cnpj = cnpj, email = email)
            if (isValidUserData(updatedUser)) {
                _isLoading.value = true
                viewModelScope.launch {
                    try {
                        val success = updateUserUseCase(updatedUser)
                        _isLoading.value = false
                        _isSaveSuccessful.value = success

                        if (success) {
                            _user.value = updatedUser
                        } else {
                            _errorMessage.value = "Falha ao salvar os dados. Tente novamente."
                        }
                    } catch (e: Exception) {
                        _isLoading.value = false
                        _isSaveSuccessful.value = false
                        _errorMessage.value = "Erro: ${e.message}"
                    }
                }
            } else {
                _errorMessage.value = "Dados inválidos. Verifique as entradas."
            }
        } else {
            _errorMessage.value = "Usuário não carregado."
        }
    }

    private fun isValidUserData(user: User): Boolean {
        return user.name.isNotEmpty() &&
                ValidationUtils.isValidCNPJ(user.cnpj) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()
    }
}