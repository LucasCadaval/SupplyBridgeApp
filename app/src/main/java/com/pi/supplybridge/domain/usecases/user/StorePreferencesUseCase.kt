package com.pi.supplybridge.domain.usecases.user

import android.util.Log
import com.pi.supplybridge.data.cache.UserPreferences
import com.pi.supplybridge.domain.enums.UserType
import kotlinx.coroutines.flow.first

data class UserInfo(
    val userId: String?,
    var userName: String?,
    val userType: UserType?
)

class StorePreferencesUseCase(private val userPreferences: UserPreferences) {
    suspend fun saveUserInfo(storeId: String, storeName: String, userType:
    String) {
        userPreferences.saveUserInfo(storeId, storeName, userType)
    }

    suspend fun getUserInfo(): UserInfo {
        val userId = userPreferences.userId.first()
        val userName = userPreferences.userName.first()
        val userTypeString = userPreferences.userType.first()

        Log.d("StorePreferencesUseCase", "Retrieved userId: $userId, userName: $userName, userType: $userTypeString")

        val userType = UserType.fromString(userTypeString)

        return UserInfo(userId, userName, userType)
    }

}
