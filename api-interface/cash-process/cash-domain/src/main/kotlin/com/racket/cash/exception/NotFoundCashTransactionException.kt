package com.racket.cash.exception

class NotFoundCashTransactionException: RuntimeException() {
    override val message = "Cash Transaction Is Not Exist"
}