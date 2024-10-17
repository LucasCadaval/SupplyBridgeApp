package com.pi.supplybridge.domain.models

import com.pi.supplybridge.domain.enums.UserType
import java.util.Date

data class User(
    var uid: String = "",
    val name: String = "",
    val email: String = "",
    val cnpj: String = "",
    val userType: UserType = UserType.STORE,
    val phoneNumber: String = "",
    val address: String = "",
    val createdAt: Date = Date(),
    val isActive: Boolean = true,
    val password: String = ""
)