package com.racket.cash.exception

class ChargingProcessingException: RuntimeException() {
    override val message: String = "Failed to Update Database as charging completed"
}