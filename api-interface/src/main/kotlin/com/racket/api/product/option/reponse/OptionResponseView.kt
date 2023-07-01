package com.racket.api.product.option.reponse

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