package com.pi.supplybridge.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.domain.usecases.user.UpdateUserUseCase
import com.pi.supplybridge.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUser(user: User) {
        _user.value = user
    }

    fun updateUser(name: String, cnpj: String, email: String) {
        val currentUser = _user.value
        if (currentUser != null) {
            val updatedUser = currentUser.copy(name = name, cnpj = cnpj, email = email)
            if (isValidUserData(updatedUser)) {
                _isLoading.value = true
                viewModelScope.launch {
                    val success = updateUserUseCase(updatedUser)
                    _isLoading.value = false
                    if (success) {
                        _user.value = updatedUser
                    }
                }
            }
        }
    }

    private fun isValidUserData(user: User): Boolean {
        return (user.name.isNotEmpty() &&
                ValidationUtils.isValidCNPJ(user.cnpj) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches())
    }
}
