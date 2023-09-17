package com.racket.api.cart.request

import com.racket.api.cart.vo.CartItemRequestVO

data class CartCreateRequestCommand(
    val userId: Long,
    val itemList: List<CartItemRequestVO>
) {

    fun validate() {
        if (this.itemList.isEmpty()) {
            throw IllegalArgumentException("item list must be not null.")
        }
        if (this.itemList.any { it.orderQuantity < 0 }) {
            throw IllegalArgumentException("quantity is must be greater than zero.")
        }
    }

}



