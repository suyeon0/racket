package com.racket.api.product.catalog

import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.vo.ProductCatalogContents

interface ProductCatalogService {
    fun addProductCatalog(productId: String, contents: ProductCatalogContents): ProductCatalogResponseView
    fun getCatalogByProductId(productId: String): ProductCatalogResponseView

}