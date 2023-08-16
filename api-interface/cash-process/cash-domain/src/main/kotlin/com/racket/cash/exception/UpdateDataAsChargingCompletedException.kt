package com.racket.cash.exception

class UpdateDataAsChargingCompletedException: RuntimeException() {
    override val message: String = "Failed to Update Database as charging completed"
}