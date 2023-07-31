package com.racket.api.admin.product.exception

class DuplicateProductException(customerProductCode: String): RuntimeException() {
    override val message: String = "This product already exists -${customerProductCode}"
}