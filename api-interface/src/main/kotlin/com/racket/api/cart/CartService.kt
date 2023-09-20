package com.racket.api.cart

import com.racket.api.cart.response.CartResponseView
import com.racket.api.cart.vo.CartItemRequestVO

interface CartService {

    fun addItem(item: CartItemRequestVO): CartResponseView

    fun deleteItem(cartItemId: Long)

    fun getItemListByUserId(userId: Long): List<CartResponseView>

    fun updateOrderQuantity(cartItemId: Long, quantity: Long): CartResponseView

}