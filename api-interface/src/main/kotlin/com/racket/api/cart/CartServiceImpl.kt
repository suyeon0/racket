package com.racket.api.cart

import com.racket.api.cart.client.DeliveryClient
import com.racket.api.cart.client.ProductClient
import com.racket.api.cart.domain.Cart
import com.racket.api.cart.domain.CartRepository
import com.racket.api.cart.exception.CartStockException
import com.racket.api.cart.vo.CartItemRequestVO
import com.racket.api.shared.user.BaseUserComponent
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.IllegalArgumentException

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val baseUserComponent: BaseUserComponent,
    private val productClient: ProductClient
    //private val deliveryClient: DeliveryClient
) : CartService {
    override fun addItem(request: CartItemRequestVO) {
        // 1. validate user
        this.baseUserComponent.validateUserByUserId(userId = request.userId)

        // 2. stock
        val option = this.productClient.getOption(optionId = request.optionId).body ?: throw Exception("option not found")
        val availableStock = option.stock
        if (availableStock < request.orderQuantity) {
           throw CartStockException(optionId = option.id)
        }

        // 3. delivery info
        //val deliveryInfo = this.deliveryClient.getInfo()

        val cart = Cart(
            userId = request.userId,
            productId = request.productId,
            optionId = request.optionId,
            originPrice = option.additionalPrice,
            calculatedPrice = request.price,
            orderQuantity = request.orderQuantity,
            deliveryCost = 0L,
            estimatedDeliveryDate = LocalDate.now()
        )
        this.cartRepository.save(cart)
    }

    override fun deleteItem(cartItemId: Long) {
        val cartItem = this.cartRepository.findById(cartItemId)
        if (cartItem.isPresent) {
            this.cartRepository.delete(cartItem.get())
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