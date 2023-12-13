package com.racket.api.delivery.common.exception

class InvalidDeliveryCompanyTypeException: RuntimeException() {
    override val message: String = "invalid delivery Company!"
}