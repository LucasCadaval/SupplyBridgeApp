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
            val result = auth.createUserWithEmailAndPassword(user.email, user
                .password).await()
            user.uid = result.user?.uid ?: return false

            usersCollection
                .document(user.uid)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun updateUser(user: User): Boolean {
        return try {
            val updates = mapOf(
                "name" to user.name,
                "cnpj" to user.cnpj,
                "email" to user.email
            )
            usersCollection.document(user.uid)
                .update(updates)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
