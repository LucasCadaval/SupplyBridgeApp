package com.pi.supplybridge.domain.usecases.user

import com.pi.supplybridge.data.repositories.UserRepository
import com.pi.supplybridge.domain.models.User

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): User? {
        return userRepository.getUserById(userId)
    }
}
