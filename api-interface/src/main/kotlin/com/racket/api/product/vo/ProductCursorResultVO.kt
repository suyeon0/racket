package com.racket.api.product.vo

import com.racket.api.product.response.ProductResponseView

class ProductCursorResultVO(
    val values: List<ProductResponseView>,
    val hasNextCursor: Boolean
)