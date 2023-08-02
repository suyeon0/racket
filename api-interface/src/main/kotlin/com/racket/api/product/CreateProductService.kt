package com.racket.api.product

import com.racket.api.product.presentation.response.ProductResponseView

interface CreateProductService: ProductService {

    fun registerProduct(productRegisterDTO: ProductRegisterDTO): ProductResponseView

    data class ProductRegisterDTO(
        val customerProductCode: String,
        val name: String,
        val price: Long
    )

}