package com.racket.api.product

import com.racket.api.product.presentation.response.ProductResponseView
import org.bson.types.ObjectId

interface CreateProductService: ProductService {

    fun registerProduct(productRegisterDTO: ProductRegisterDTO): ProductResponseView

    data class ProductRegisterDTO(
        val id: String = ObjectId().toHexString(),
        val name: String,
        val price: Long
    )

}