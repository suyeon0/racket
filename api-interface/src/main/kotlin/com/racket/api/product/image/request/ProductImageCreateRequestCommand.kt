package com.racket.api.product.image.request

data class ProductImageCreateRequestCommand (
    val productId: String,
    val imageUrls: List<String>
)