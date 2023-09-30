package com.racket.delivery.exception

class InvalidDeliveryCompanyTypeException: RuntimeException() {
    override val message: String = "invalid delivery Company!"
}