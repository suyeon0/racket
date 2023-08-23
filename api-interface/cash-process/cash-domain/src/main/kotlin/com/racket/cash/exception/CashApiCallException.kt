package com.racket.cash.exception

class CashApiCallException: RuntimeException() {
    override val message = "Cash Api Call Failed"
}