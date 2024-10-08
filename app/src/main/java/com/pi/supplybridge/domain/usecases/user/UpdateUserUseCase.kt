package com.pi.supplybridge.domain.usecases.user

import com.pi.supplybridge.domain.models.User
import com.pi.supplybridge.data.repositories.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Boolean {
        return userRepository.updateUser(user)
    }
}
