package com.racket.api.product.option.response

import io.swagger.v3.oas.annotations.media.Schema

data class OptionResponseView(
    @Schema(title = "옵션 ID", example = "1")
    val id: String,
    @Schema(title = "상품 ID", example = "10")
    val productId: String,
    @Schema(title = "옵션명", example = "V14 골드 315g")
    val name: String,
    @Schema(title = "옵션추가금액", example = "10000")
    val price: Long,
    @Schema(title = "재고수량합계", example = "50")
    val stock: Int,
    @Schema(title = "부가설명", example = "부가설명입니다")
    val description: String?,
    @Schema(title = "정렬순서", example = "0", minimum = "0")
    val sort: Int,
    @Schema(title = "사용여부", example = "true")
    val displayYn: Boolean,
)