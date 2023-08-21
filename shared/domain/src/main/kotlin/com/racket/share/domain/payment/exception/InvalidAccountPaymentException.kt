package com.racket.share.domain.payment.exception

class InvalidAccountPaymentException(message: String): RuntimeException() {
    override val message = message
}