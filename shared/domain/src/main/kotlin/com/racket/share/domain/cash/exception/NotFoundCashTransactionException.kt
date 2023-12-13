package com.racket.share.domain.cash.exception

class NotFoundCashTransactionException: RuntimeException() {
    override val message = "Cash Transaction Is Not Exist"
}