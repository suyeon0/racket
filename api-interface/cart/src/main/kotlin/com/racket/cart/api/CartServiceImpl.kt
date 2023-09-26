package com.racket.cart.api

import com.racket.cart.api.client.delivery.DeliveryClient
import com.racket.cart.api.client.delivery.DeliveryResponseView
import com.racket.cart.api.client.product.ProductClient
import com.racket.cart.api.domain.Cart
import com.racket.cart.api.domain.CartRepository
import com.racket.cart.api.exception.CartDeliveryFeignException
import com.racket.cart.api.exception.CartStockException
import com.racket.cart.api.exception.NotFoundCartItemException
import com.racket.api.cart.response.CartResponseView
import com.racket.cart.api.vo.CartItemRequestVO
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.shared.user.BaseUserComponent
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val baseUserComponent: BaseUserComponent,
    private val productClient: ProductClient,
    private val deliveryClient: DeliveryClient
) : CartService {

    @Transactional
    override fun addItem(item: CartItemRequestVO): CartResponseView {
        val optionId = item.optionId

        // 1. validate user
        this.baseUserComponent.validateUserByUserId(userId = item.userId)

        // 2. stock
        val availableStock = this.productClient.getOption(optionId = optionId).body?.stock ?: throw Exception("option not found")
        if (availableStock < item.orderQuantity) {
            throw CartStockException(optionId = optionId)
        }

        // 3. delivery info
        val deliveryResponse: DeliveryResponseView
        try {
            deliveryResponse = this.deliveryClient.get(optionId = optionId)?.body!!
            if(deliveryResponse.statusCode.toInt() != HttpStatus.OK.value()) {
                throw CartDeliveryFeignException(deliveryResponse.statusMessage)
            }
        } catch (e: Exception) {
            throw CartDeliveryFeignException("delivery api call fail!")
        }

        val cart = Cart(
            userId = item.userId,
            productId = item.productId,
            optionId = optionId,
            originalPrice = item.originalPrice,
            calculatedPrice = item.calculatedPrice,
            orderQuantity = item.orderQuantity,
            deliveryCost = deliveryResponse.deliveryCost,
            estimatedDeliveryDate = deliveryResponse.expectedDate
        )
        return CartResponseView.makeView(this.cartRepository.save(cart))
    }

    @Transactional
    override fun deleteItem(cartItemId: Long) =
        this.cartRepository.delete(
            this.cartRepository.findById(cartItemId).orElseThrow { NotFoundCartItemException(cartItemId = cartItemId) })

    override fun getItemListByUserId(userId: Long): List<CartResponseView> =
        this.cartRepository.findAllByUserId(userId).stream().map { item -> CartResponseView.makeView(item) }.toList()

    @Transactional
    override fun updateOrderQuantity(cartItemId: Long, orderQuantity: Long): CartResponseView {
        val cartItem = this.cartRepository.findById(cartItemId).orElseThrow { NotFoundCartItemException(cartItemId = cartItemId) }

        val availableStock =
            this.productClient.getOption(optionId = cartItem.optionId).body?.stock ?: throw NotFoundOptionException()
        if (availableStock < orderQuantity) {
            throw CartStockException(optionId = cartItem.optionId)
        }

        cartItem.updateOrderQuantity(quantity = orderQuantity)
        return CartResponseView.makeView(this.cartRepository.save(cartItem))
    }
}