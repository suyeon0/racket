package com.racket.api.admin.product.presentation.request

data class ProductCreateRequestCommand(

    val productName: String,

    val productPrice: Long
) {
    fun validate() {
        if(this.productName.isEmpty()) {
            throw IllegalArgumentException("Product Name cannot be empty")
        }
        if(this.productPrice < 0) {
            throw IllegalArgumentException("Product Price must be bigger than zero")
        }
    }

}