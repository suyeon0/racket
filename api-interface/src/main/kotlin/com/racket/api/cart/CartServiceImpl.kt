package com.racket.api.cart

import com.racket.api.cart.client.ProductClient
import com.racket.api.cart.domain.Cart
import com.racket.api.cart.domain.CartRepository
import com.racket.api.cart.exception.CartStockException
import com.racket.api.cart.response.CartResponseView
import com.racket.api.cart.vo.CartItemRequestVO
import com.racket.api.product.exception.NotFoundProductException
import com.racket.api.shared.user.BaseUserComponent
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.IllegalArgumentException
import kotlin.streams.toList

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val baseUserComponent: BaseUserComponent,
    private val productClient: ProductClient
    //private val deliveryClient: DeliveryClient
) : CartService {
    override fun addItem(item: CartItemRequestVO): CartResponseView {
        // 1. validate user
        this.baseUserComponent.validateUserByUserId(userId = item.userId)

        // 2. stock
        val option = this.productClient.getOption(optionId = item.optionId).body ?: throw Exception("option not found")
        val availableStock = option.stock
        if (availableStock < item.orderQuantity) {
            throw CartStockException(optionId = option.id)
        }

        // 3. delivery info
        //val deliveryInfo = this.deliveryClient.getInfo()

        val cart = Cart(
            userId = item.userId,
            productId = item.productId,
            optionId = item.optionId,
            originPrice = option.additionalPrice,
            calculatedPrice = item.price,
            orderQuantity = item.orderQuantity,
            deliveryCost = 0L,
            estimatedDeliveryDate = LocalDate.now()
        )
        return CartResponseView.makeView(this.cartRepository.save(cart))
    }

    override fun deleteItem(cartItemId: Long) =
        this.cartRepository.delete(this.cartRepository.findById(cartItemId).orElseThrow { NotFoundProductException() })

    override fun getItemListByUserId(userId: Long): List<CartResponseView> =
        this.cartRepository.findAllByUserId(userId).stream().map { item -> CartResponseView.makeView(item) }.toList()


    override fun updateOrderQuantity(cartItemId: Long, quantity: Long): CartResponseView {
        val cartItem = this.cartRepository.findById(cartItemId).orElseThrow { NotFoundProductException() }
        cartItem.updateOrderQuantity(quantity = quantity)
        return CartResponseView.makeView(this.cartRepository.save(cartItem))
    }
}