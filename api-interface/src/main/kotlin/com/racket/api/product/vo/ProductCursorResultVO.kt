package com.racket.api.product.vo

import com.racket.api.product.presentation.response.ProductResponseView

class ProductCursorResultVO(
    val productResponseViewList: List<ProductResponseView>,

    val hasNextCursor: Boolean
)