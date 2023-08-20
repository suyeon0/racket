package com.racket.cash.exception

class InvalidChargingTransactionException: RuntimeException() {
    override val message: String = "this charging transaction is invalid."
}