package com.racket.cash.presentation.request

data class CreateChargeCommand(
    val userId: Long,
    val amount: Long,
    val userChargingWayId: Long
) {
    fun validate() {
        if (this.amount < 100_000) {
            throw IllegalArgumentException("Charge amount must be greater than 100,000")
        }
    }
}