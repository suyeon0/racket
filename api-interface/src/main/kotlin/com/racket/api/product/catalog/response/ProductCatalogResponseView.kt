package com.racket.api.product.catalog.response

import com.racket.api.product.vo.ProductCatalogContents

data class ProductCatalogResponseView(
    val id: Long,
    val productId: String,
    val contents: ProductCatalogContents
)