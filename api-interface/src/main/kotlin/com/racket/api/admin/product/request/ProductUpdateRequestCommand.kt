package com.racket.api.admin.product.request

data class ProductUpdateRequestCommand(
    val productName: String,

    val productPrice: Long
) {
    fun validate() {
        if(this.productName.isEmpty()) {
            throw IllegalArgumentException("product name cannot be empty")
        }
        if(this.productPrice < 0) {
            throw IllegalArgumentException("product price must be bigger than zero")
        }
    }

}