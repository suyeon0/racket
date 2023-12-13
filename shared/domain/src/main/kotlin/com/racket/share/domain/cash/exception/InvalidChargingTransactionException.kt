package com.racket.share.domain.cash.exception

class InvalidChargingTransactionException(message: String): RuntimeException() {
    override val message: String = "this charging transaction is invalid. reason :${message}"
}