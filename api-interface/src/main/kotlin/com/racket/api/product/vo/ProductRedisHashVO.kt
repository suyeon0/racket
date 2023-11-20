package com.racket.api.product.vo

import com.racket.api.product.domain.Product
import com.racket.api.product.domain.enums.ProductStatusType

data class ProductRedisHashVO (

    val id: String,

    var name: String,

    var price: Long,

    var status: ProductStatusType

) {

    companion object {
        fun of(product: Product): ProductRedisHashVO =
            ProductRedisHashVO (
                id = product.id!!,
                name = product.name,
                price = product.price,
                status = product.status
            )

        fun makeEntity(productVO: ProductRedisHashVO) = Product(
            id = productVO.id,
            name = productVO.name,
            price = productVO.price,
            status = productVO.status
        )
    }

}