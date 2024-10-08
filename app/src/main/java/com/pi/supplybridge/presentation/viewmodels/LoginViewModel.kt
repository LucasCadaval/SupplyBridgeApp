package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pi.supplybridge.data.repositories.AuthRepository
import com.pi.supplybridge.data.services.FirebaseService
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val service: FirebaseService
) : ViewModel() {
    private val tag = "LoginViewModel"

    suspend fun login(email: String, password: String): String? {
        return try {
            Log.d(tag, "Attempting to log in with email: $email")
            val userId = authRepository.login(email, password)
            Log.d(tag, "Login successful for user ID: $userId")
            userId
        } catch (e: Exception) {
            Log.e(tag, "Login failed for email $email", e)
            null
        }
    }

    suspend fun getUserType(userId: String): String? {
        return try {
            Log.d(tag, "Fetching user type for user ID: $userId")
            val document = service.firestoreInstance.collection("users").document(userId).get()
                .await()
            val userType = document.getString("userType")
            Log.d(tag, "User type retrieved: $userType")
            userType
        } catch (e: Exception) {
            Log.e(tag, "Failed to fetch user type for user ID: $userId", e)
            null
        }
    }

}
