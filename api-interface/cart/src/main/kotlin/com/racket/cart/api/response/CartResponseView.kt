package com.racket.cart.api.response

import com.racket.cart.api.domain.Cart
import com.racket.cart.api.vo.DeliveryInformationVO
import java.time.LocalDate

data class CartResponseView(
    val id: Long,

    val userId: Long,

    val productId: Long,

    val optionId: Long,

    val originalPrice: Long,

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
                originalPrice = cart.originalPrice,
                calculatedPrice = cart.calculatedPrice,
                orderQuantity = cart.orderQuantity,
                deliveryInformationVO = DeliveryInformationVO(
                    deliveryCost = cart.deliveryCost,
                    expectedDate = LocalDate.now().plusDays(cart.estimatedDeliveryDays)
                )
            )
    }

}