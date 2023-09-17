package com.racket.api.cart

import com.racket.api.cart.client.ProductClient
import com.racket.api.cart.domain.Cart
import com.racket.api.cart.domain.CartRepository
import com.racket.api.cart.vo.CartItemRequestVO
import com.racket.api.shared.user.BaseUserComponent
import org.springframework.stereotype.Service
import kotlin.IllegalArgumentException

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val baseUserComponent: BaseUserComponent,
    private val productClient: ProductClient
) : CartService {
    override fun addItem(userId: Long, item: CartItemRequestVO) {
        // 1. validate user
        val user = this.baseUserComponent.validateUserByUserId(userId = userId)

        // 2. get option
        val option = this.productClient.getOption(optionId = item.optionId).body

        val cart = Cart(
            userId = userId,
            optionId = item.optionId,
            originPrice = option.additionalPrice,
            calculatedPrice = item.
        )
        this.cartRepository.save(cart)


    }

    override fun deleteItem(cartItemId: Long) {
        val cartItem = cartRepository.findById(cartItemId)
        if (cartItem.isPresent) {
            cartRepository.delete(cartItem.get())
        } else {
            throw IllegalArgumentException("Cart item not found")
        }
    }

    override fun getItemListByUserId(userId: Long) {
        val cartItemList = this.cartRepository.findAllByUserId(userId)
    }

    override fun updateOrderQuantity(cartItemId: Long, quantity: Long) {
        val cartItem = this.cartRepository.findById(cartItemId)
        val updatedCartItem = cartItem.orElseThrow { IllegalArgumentException("Cart item not found") }

        updatedCartItem.updateOrderQuantity(quantity = quantity)
        this.cartRepository.save(updatedCartItem)
    }
}