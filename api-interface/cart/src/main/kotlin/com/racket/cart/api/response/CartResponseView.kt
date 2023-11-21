package com.racket.cart.api.response

import com.racket.cart.api.domain.Cart
import com.racket.cart.api.vo.DeliveryInformationVO

data class CartResponseView(
    val id: Long,

    val userId: Long,

    val productId: String,

    val optionId: String,

    val productName: String,

    val optionName: String,

    val productRepresentativeImage: String,

    val originalPrice: Long,

    val calculatedPrice: Long,

    var orderQuantity: Long,

    val deliveryCost: Long
) {

    companion object {
        fun makeView(cart: Cart) =
            CartResponseView(
                id = cart.id!!,
                userId = cart.userId,
                productId = cart.productId,
                optionId = cart.optionId,
                originalPrice = cart.originalPrice,
                calculatedPrice = cart.calculatedPrice,
                orderQuantity = cart.orderQuantity,
                productName = cart.productName,
                optionName = cart.optionName,
                productRepresentativeImage = cart.productRepresentativeImage,
                deliveryCost = cart.deliveryCost
            )
    }

}