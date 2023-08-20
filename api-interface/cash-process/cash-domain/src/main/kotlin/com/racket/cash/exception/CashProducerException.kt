package com.racket.cash.exception

class CashProducerException(event: Any, exception: Exception): RuntimeException() {

    override val message: String = "Cash Produce Event Failed. -${event}, exception-${exception}"
}