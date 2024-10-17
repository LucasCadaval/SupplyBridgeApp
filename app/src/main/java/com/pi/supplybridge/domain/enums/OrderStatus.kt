package com.pi.supplybridge.domain.enums

enum class OrderStatus(val description: String) {
    OPEN("Aberto"),
    NEGOTIATION("Em Negociação"),
    FINALIZED("Finalizado"),
    CANCELLED("Cancelado");
}