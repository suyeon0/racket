package com.racket.cart.api

import com.racket.cart.api.client.delivery.DeliveryClient
import com.racket.cart.api.client.delivery.DeliveryResponseView
import com.racket.cart.api.client.product.ProductClient
import com.racket.cart.api.domain.Cart
import com.racket.cart.api.domain.CartRepository
import com.racket.cart.api.vo.CartItemRequestVO
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.shared.user.BaseUserComponent
import com.racket.cart.api.exception.*
import com.racket.cart.api.response.CartResponseView
import mu.KotlinLogging
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

    private val log = KotlinLogging.logger { }

    /**
     * 장바구니에는 그 시점 시냅샷 데이터를 담는다 (가용재고 체크, 상품 가격, 배송 정보)
     * - 주문서 작성 시점에 데이터 재조회 : 가용재고 체크, 상품 가격, 배송 정보
     * - 품절 반영 일배치
     */
    @Transactional
    override fun addItem(item: CartItemRequestVO): CartResponseView {
        val optionId = item.optionId

        // 1. validate user
        this.validateUser(userId = item.userId)
        // TODO: Shared 에서 발생하는 exception handle 방법..?

        // 2. stock
        this.validateAvailableStock(optionId = optionId, orderQuantity = item.orderQuantity)

        // 3. delivery info
        val deliveryResponse = this.getDeliveryInfoByOptionId(optionId = optionId)

        val cart = Cart(
            userId = item.userId,
            productId = item.productId,
            optionId = optionId,
            originalPrice = item.originalPrice,
            calculatedPrice = item.calculatedPrice,
            orderQuantity = item.orderQuantity,
            deliveryCost = deliveryResponse.deliveryCost,
            estimatedDeliveryDays = deliveryResponse.deliveryDays
        )
        return CartResponseView.makeView(this.cartRepository.save(cart))
    }

    private fun validateUser(userId: Long) {
        try {
            this.baseUserComponent.validateUserByUserId(userId = userId)
        } catch (e: Exception) {
            throw CartInvalidException(e.message)
        }
    }

    private fun validateAvailableStock(optionId: Long, orderQuantity: Long) {
        try {
            val availableStock =
                this.productClient.getOption(optionId = optionId).body?.stock ?: throw Exception("option not found")
            if (availableStock < orderQuantity) {
                throw CartStockException(optionId = optionId)
            }
        } catch (e: Exception) {
            log.error { "error:::${e}" }
            throw CartProductFeignException("product api call fail!")
        }
    }

    private fun getDeliveryInfoByOptionId(optionId: Long): DeliveryResponseView {
        try {
            val deliveryResponse = this.deliveryClient.get(optionId = optionId)?.body!!
            if (deliveryResponse.statusCode.toInt() != HttpStatus.OK.value()) {
                throw CartDeliveryFeignException(deliveryResponse.statusMessage)
            }
            return deliveryResponse
        } catch (e: CartDeliveryFeignException) {
            log.error { "error:::${e}" }
            throw e
        } catch (e: Exception) {
            log.error { "error:::${e}" }
            throw CartDeliveryFeignException("delivery api call fail! - ${e.message}")
        }
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