package com.racket.cart.api

import com.racket.api.cart.response.CartResponseView
import com.racket.cart.api.vo.CartItemRequestVO

interface CartService {

    fun addItem(item: CartItemRequestVO): CartResponseView

    fun deleteItem(cartItemId: Long)

    fun getItemListByUserId(userId: Long): List<CartResponseView>

    fun updateOrderQuantity(cartItemId: Long, orderQuantity: Long): CartResponseView

}