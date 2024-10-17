package com.pi.supplybridge.data.repositories

import com.pi.supplybridge.data.services.FirebaseService
import com.pi.supplybridge.domain.models.User
import kotlinx.coroutines.tasks.await

class UserRepository(private val service: FirebaseService) {
    private val usersCollection = service.firestoreInstance
        .collection("users")

    private val auth = service.authInstance

    suspend fun getUserById(userId: String): User? {
        return try {
            val document = usersCollection.document(userId).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUser(user: User): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            user.uid = result.user?.uid ?: return false

            usersCollection
                .document(user.uid)
                .set(user.toMap())
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUser(user: User): Boolean {
        return try {
            val updates = user.toMap().filter { it.value != null }
            usersCollection.document(user.uid)
                .update(updates)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun User.toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "cnpj" to cnpj,
            "userType" to userType.name,
            "phoneNumber" to phoneNumber,
            "address" to address,
            "createdAt" to createdAt,
            "isActive" to isActive
        )
    }
}
