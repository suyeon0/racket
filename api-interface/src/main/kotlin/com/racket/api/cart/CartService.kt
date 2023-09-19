package com.racket.api.cart

import com.racket.api.cart.vo.CartItemRequestVO

interface CartService {

    fun addItem(item: CartItemRequestVO)

    fun deleteItem(cartItemId: Long)

    fun getItemListByUserId(userId: Long)

    fun updateOrderQuantity(cartItemId: Long, quantity: Long)

}