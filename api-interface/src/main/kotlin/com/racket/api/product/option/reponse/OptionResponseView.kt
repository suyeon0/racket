package com.racket.api.product.option.reponse

import io.swagger.v3.oas.annotations.media.Schema

data class OptionResponseView(
    @Schema(title = "옵션 ID", example = "1")
    val id: Long,
    @Schema(title = "고객사 옵션 번호", example = "10_1")
    val optionNo: String,
    @Schema(title = "상품 ID", example = "10")
    val productId: Long,
    @Schema(title = "옵션명", example = "V14 골드 315g")
    val name: String,
    @Schema(title = "옵션추가금액", example = "10000")
    val additionalPrice: Long,
    @Schema(title = "재고수량합계", example = "50")
    val stock: Int,
    @Schema(title = "비고", example = "")
    val remark: String,
    @Schema(title = "정렬순서", example = "0", minimum = "0")
    val sort: Int
)