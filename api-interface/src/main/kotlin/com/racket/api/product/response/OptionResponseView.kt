package com.racket.api.product.response

data class OptionResponseView(
    val id: Long,
    val optionNo: String,
    val productId: Long,
    val name: String,
    val additionalPrice: Long,
    val price: Long,
    val stock: Int,
    val remark: String
)