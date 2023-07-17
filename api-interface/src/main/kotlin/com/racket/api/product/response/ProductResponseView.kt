package com.racket.api.product.response

import io.swagger.v3.oas.annotations.media.Schema

data class ProductResponseView(
    @Schema(example = "1234567")
    val id: Long,
    @Schema(example = "윌슨 클래시 100 V2 WR074011 테니스라켓")
    val name: String,
    @Schema(example = "167500")
    val price: Long
)