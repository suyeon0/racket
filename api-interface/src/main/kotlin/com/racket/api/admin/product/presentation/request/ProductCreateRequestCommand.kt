package com.racket.api.admin.product.presentation.request

data class ProductCreateRequestCommand(
    val customerProductCode: String,

    val productName: String,

    val productPrice: Long
) {
    fun validate() {
        if(this.customerProductCode.isEmpty()) {
            throw IllegalArgumentException("Customer Product Code cannot be empty")
        }
        if(this.productName.isEmpty()) {
            throw IllegalArgumentException("Product Name cannot be empty")
        }
        if(this.productPrice < 0) {
            throw IllegalArgumentException("Product Price must be bigger than zero")
        }
    }

}