package com.pi.supplybridge.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.pi.supplybridge.data.repositories.AuthRepository

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return authRepository.sendPasswordResetEmail(email)
    }
}
