package com.pi.supplybridge.data.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {

    private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "user_preferences")

    companion object {
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
        private val STORE_ID_KEY = stringPreferencesKey("store_id")
        private val STORE_NAME_KEY = stringPreferencesKey("store_name")
    }

    private val dataStore = context.dataStore

    suspend fun saveUserType(userType: String) {
        dataStore.edit { preferences ->
            preferences[USER_TYPE_KEY] = userType
        }
    }

    suspend fun saveStoreInfo(storeId: String, storeName: String) {
        dataStore.edit { preferences ->
            preferences[STORE_ID_KEY] = storeId
            preferences[STORE_NAME_KEY] = storeName
        }
    }

    val userType: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_TYPE_KEY]
        }

    val storeId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[STORE_ID_KEY]
        }

    val storeName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[STORE_NAME_KEY]
        }
}
