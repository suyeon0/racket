package com.racket.api.product.vo

import org.bson.types.ObjectId

data class ProductRegisterVO(
    val id: String = ObjectId().toHexString(),
    val name: String,
    val price: Long
)
