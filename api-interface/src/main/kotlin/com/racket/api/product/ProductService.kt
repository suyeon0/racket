package com.racket.api.product

import com.racket.api.product.domain.Product
import com.racket.api.product.presentation.response.ProductResponseView

interface ProductService {

    fun makeProductResponseViewFromProduct(product: Product) =
        ProductResponseView(
            id = product.id,
            name = product.name,
            price = product.price,
            statusType = product.status
        )
}