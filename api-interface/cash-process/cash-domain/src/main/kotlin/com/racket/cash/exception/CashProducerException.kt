package com.racket.cash.exception

class CashProducerException(event: Any): RuntimeException() {

    override val message: String = "Cash Produce Event Failed. -${event}"
}