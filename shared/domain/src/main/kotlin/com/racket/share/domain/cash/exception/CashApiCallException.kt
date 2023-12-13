package com.racket.share.domain.cash.exception

class CashApiCallException: RuntimeException() {
    override val message = "Cash Api Call Failed"
}