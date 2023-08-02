package com.racket.api.product

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.presentation.response.ProductResponseView

interface UpdateProductService: ProductService {

    fun updateProductInfo(id: Long, name: String, price: Long): ProductResponseView

    fun updateProductStatus(id: Long, status: ProductStatusType): ProductResponseView

}