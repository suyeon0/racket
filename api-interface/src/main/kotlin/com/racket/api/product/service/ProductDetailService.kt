package com.racket.api.product.service

import com.racket.api.product.catalog.response.ProductCatalogResponseView
import com.racket.api.product.response.ProductDetailResponseView

interface ProductDetailService {

    fun getProductDetail(productId: String): ProductDetailResponseView

    fun getCatalog(productId: String): ProductCatalogResponseView
}