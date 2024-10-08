package com.pi.supplybridge.domain.models

data class User(
    var uid: String = "",
    val name: String = "",
    val email: String = "",
    val cnpj: String = "",
    val userType: String = "",
    val password: String = ""
)
