package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pi.supplybridge.data.repositories.AuthRepository
import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.usecases.user.StorePreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val service: FirebaseService,
    private val storePreferencesUseCase: StorePreferencesUseCase
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

    suspend fun getUserDetails(userId: String): String? {
        return try {
            Log.d(tag, "Fetching user details for user ID: $userId")
            val document = service.firestoreInstance.collection("users").document(userId).get().await()

            val userType = document.getString("userType")
            val storeId = document.getString("storeId")
            val storeName = document.getString("storeName")

            CoroutineScope(Dispatchers.IO).launch {
                userType?.let { storePreferencesUseCase.saveUserType(it) }
                if (storeId != null && storeName != null) {
                    storePreferencesUseCase.saveStoreInfo(storeId, storeName)
                }
            }

            Log.d(tag, "User details retrieved - UserType: $userType, StoreId: $storeId, StoreName: $storeName")
            userType
        } catch (e: Exception) {
            Log.e(tag, "Failed to fetch user details for user ID: $userId", e)
            null
        }
    }
}
