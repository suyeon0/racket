package com.racket.api.product

import com.racket.api.product.domain.enums.ProductStatusType
import com.racket.api.product.presentation.response.ProductResponseView

interface UpdateProductService: ProductService {

    fun updateProductInfo(id: String, name: String, price: Long): ProductResponseView

    fun updateProductStatus(id: String, status: ProductStatusType): ProductResponseView

}