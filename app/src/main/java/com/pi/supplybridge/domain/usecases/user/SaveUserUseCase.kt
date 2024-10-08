package com.pi.supplybridge.domain.usecases.user

import android.util.Log
import com.pi.supplybridge.data.repositories.UserRepository
import com.pi.supplybridge.domain.models.User

class SaveUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Boolean {
        return try {
            userRepository.saveUser(user)
            true
        } catch (e: Exception) {
            Log.e("SaveUserUseCase", e.message.toString())
            false
        }
    }
}
