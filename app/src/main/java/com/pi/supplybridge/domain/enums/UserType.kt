package com.pi.supplybridge.domain.enums

enum class UserType(val description: String) {
    SUPPLIER("Fornecedor"),
    STORE("Loja");

    companion object {
        fun fromString(value: String?): UserType? {
            return entries.find { it.name == value }
        }
    }
}