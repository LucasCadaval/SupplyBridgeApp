package com.pi.supplybridge.data.cache

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.pi.supplybridge.domain.enums.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    private val dataStore = context.dataStore

    companion object {
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    suspend fun saveUserInfo(userId: String, userName: String, userType: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_NAME_KEY] = userName
            preferences[USER_TYPE_KEY] = userType
        }
        Log.d("UserPreferences", "User info saved: userId=$userId, userName=$userName, userType=$userType")
    }


    val userType: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_TYPE_KEY]
        }

    val userId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    val userName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]
        }
}
