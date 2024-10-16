package com.pi.supplybridge.domain.usecases.user

import com.pi.supplybridge.data.cache.UserPreferences
import kotlinx.coroutines.flow.first

class StorePreferencesUseCase(private val userPreferences: UserPreferences) {

    suspend fun saveStoreInfo(storeId: String, storeName: String) {
        userPreferences.saveStoreInfo(storeId, storeName)
    }

    suspend fun getStoreInfo(): Pair<String?, String?> {
        val storeId = userPreferences.storeId.first()
        val storeName = userPreferences.storeName.first()
        return Pair(storeId, storeName)
    }

    suspend fun saveUserType(userType: String) {
        userPreferences.saveUserType(userType)
    }

    suspend fun getUserType(): String? {
        return userPreferences.userType.first()
    }
}
