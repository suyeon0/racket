package com.racket.share.domain.cash.exception

class ChargingProcessingException: RuntimeException() {
    override val message: String = "Failed to Update Database as charging completed"
}