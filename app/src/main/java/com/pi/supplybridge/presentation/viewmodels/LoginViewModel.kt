package com.pi.supplybridge.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pi.supplybridge.data.repositories.AuthRepository
import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.enums.UserType
import com.pi.supplybridge.domain.usecases.user.StorePreferencesUseCase
import com.pi.supplybridge.domain.usecases.user.UserInfo
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

            if (userId != null) {
                getUserDetails(userId)
            } else {
                Log.e(tag, "Failed to retrieve userId after login")
            }

            userId
        } catch (e: Exception) {
            Log.e(tag, "Login failed for email $email", e)
            null
        }
    }

    suspend fun getUserDetails(userId: String): UserInfo? {
        return try {
            Log.d(tag, "Fetching user details for user ID: $userId")
            val document = service.firestoreInstance.collection("users").document(userId).get().await()

            val userTypeString = document.getString("userType")
            val userName = document.getString("name")

            if (userName == null) {
                Log.e(tag, "Error: userName is missing in Firestore for user ID: $userId")
            }
            if (userTypeString == null) {
                Log.e(tag, "Error: userType is missing in Firestore for user ID: $userId")
            }

            Log.d(tag, "Fetched details - userName: $userName, userType: $userTypeString")

            viewModelScope.launch {
                if (userName != null && userTypeString != null) {
                    Log.d(tag, "Saving user info in DataStore: userId=$userId, userName=$userName, userType=$userTypeString")
                    storePreferencesUseCase.saveUserInfo(userId, userName, userTypeString)
                } else {
                    Log.e(tag, "Error: Retrieved userName or userType is null.")
                }
            }

            val userType = UserType.fromString(userTypeString)
            UserInfo(userId, userName, userType)
        } catch (e: Exception) {
            Log.e(tag, "Failed to fetch user details for user ID: $userId", e)
            null
        }
    }
}
