package com.racket.api.product

import com.racket.api.product.domain.Product
import com.racket.api.product.response.ProductResponseView

interface ProductService {

    fun makeProductResponseViewFromProduct(product: Product) =
        ProductResponseView(
            id = product.id!!,
            customerProductCode = product.customerProductCode,
            name = product.name,
            price = product.price,
            statusType = product.status
        )
}