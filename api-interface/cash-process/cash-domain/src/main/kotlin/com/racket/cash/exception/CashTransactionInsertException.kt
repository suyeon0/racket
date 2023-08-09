package com.racket.cash.exception

class CashTransactionInsertException: RuntimeException() {
    override val message: String = "Cash Transaction Insert Process is Failed."
}