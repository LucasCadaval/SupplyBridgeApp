package com.pi.supplybridge.data.repositories

import com.pi.supplybridge.data.services.FirebaseService
import kotlinx.coroutines.tasks.await

class AuthRepository(private val service: FirebaseService) {
    private val auth = service.authInstance

    suspend fun login(email: String, password: String): String? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser?.uid
        } catch (e: Exception) {
            null
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
