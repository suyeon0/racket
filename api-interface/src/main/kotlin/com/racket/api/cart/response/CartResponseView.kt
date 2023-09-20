package com.racket.api.cart.response

import com.racket.api.cart.domain.Cart
import com.racket.api.cart.vo.DeliveryInformationVO

data class CartResponseView(
    val id: Long,

    val userId: Long,

    val productId: Long,

    val optionId: Long,

    val originPrice: Long,

    val calculatedPrice: Long,

    var orderQuantity: Long,

    val deliveryInformationVO: DeliveryInformationVO
) {

    companion object {
        fun makeView(cart: Cart) =
            CartResponseView(
                id = cart.id!!,
                userId = cart.userId,
                productId = cart.productId,
                optionId = cart.optionId,
                originPrice = cart.originPrice,
                calculatedPrice = cart.calculatedPrice,
                orderQuantity = cart.orderQuantity,
                deliveryInformationVO = DeliveryInformationVO(
                    deliveryCost = cart.deliveryCost,
                    expectedDate = cart.estimatedDeliveryDate
                )
            )
    }

}