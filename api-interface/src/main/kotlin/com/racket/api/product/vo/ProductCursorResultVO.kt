package com.racket.api.product.vo

import com.racket.api.product.response.ProductResponseView
import io.swagger.v3.oas.annotations.media.Schema

class ProductCursorResultVO(
    @Schema(title = "상품 response list")
    val productResponseViewList: List<ProductResponseView>,


    @Schema(title = "다음 페이지 존재 여부", example = "true")
    val hasNextCursor: Boolean
)