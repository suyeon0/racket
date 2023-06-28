package com.racket.api.product.exception

class NotFoundProductException: RuntimeException() {
    override val message: String = "No matching product found"
}