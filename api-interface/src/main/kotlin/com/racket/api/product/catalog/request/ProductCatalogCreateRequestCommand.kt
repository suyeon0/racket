package com.racket.api.product.catalog.request

import com.racket.api.product.vo.ProductCatalogContents

data class ProductCatalogCreateRequestCommand (
    val productId: String,
    val contents: ProductCatalogContents
)