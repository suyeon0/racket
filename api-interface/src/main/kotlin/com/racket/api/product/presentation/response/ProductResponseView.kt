package com.racket.api.product.presentation.response

import com.racket.api.product.domain.enums.ProductStatusType
import io.swagger.v3.oas.annotations.media.Schema

data class ProductResponseView(
    @Schema(title = "상품 ID", example = "1234567")
    val id: Long,

    @Schema(title = "고객사 상품 코드", example = "WILSON-13456")
    val customerProductCode: String,

    @Schema(title = "상품명", example = "윌슨 테니스라켓 2023 프로스태프 V14 골드")
    val name: String,

    @Schema(title = "상품 기본가", example = "167500")
    val price: Long,

    @Schema(title = "상품 상태", example = "ACTIVE")
    val statusType: ProductStatusType
)