package com.racket.api.product

import com.racket.api.product.presentation.response.ProductDetailResponseView

interface ProductDetailService {

    fun getProductDetail(productId: String): ProductDetailResponseView?
}