package com.racket.cart.api

import com.racket.cart.api.client.product.ProductClient
import com.racket.cart.api.domain.Cart
import com.racket.cart.api.domain.CartRepository
import com.racket.cart.api.vo.CartItemRequestVO
import com.racket.api.product.exception.NotFoundOptionException
import com.racket.api.product.option.response.OptionWithProductView
import com.racket.api.shared.user.BaseUserComponent
import com.racket.cart.api.exception.*
import com.racket.cart.api.response.CartResponseView
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val baseUserComponent: BaseUserComponent,
    private val productClient: ProductClient
) : CartService {

    private val log = KotlinLogging.logger { }

    /**
     * 장바구니에는 그 시점 시냅샷 데이터를 담는다 (가용재고 체크, 상품 가격 포함한 상품 정보, 상품 이미지)
     * -> val deliveryResponse = this.getDeliveryInfoByOptionId(optionId = optionId) 삭제
     * -> 주문서 작성 시점에 데이터 재조회 : 가용재고 체크, 상품 가격, 배송 정보
     * - 품절 반영 일배치
     */
    @Transactional
    override fun addItem(item: CartItemRequestVO): CartResponseView {
        val optionId = item.optionId

        // 1. validate user
        this.validateUser(userId = item.userId)

        // 2. product, stock validation
        val itemInformation = this.getItemInformationAndValidateStock(
            productId = item.productId,
            optionId = item.optionId,
            orderQuantity = item.orderQuantity
        )

        val cart = Cart(
            userId = item.userId,
            productId = item.productId,
            optionId = optionId,
            originalPrice = item.originalPrice,
            calculatedPrice = item.calculatedPrice,
            orderQuantity = item.orderQuantity,
            productName = itemInformation.productName,
            optionName = itemInformation.name,
            productRepresentativeImage = "",
            deliveryCost = 0L
        )
        return CartResponseView.makeView(this.cartRepository.save(cart))
    }

    private fun getItemInformationAndValidateStock(
        productId: String,
        optionId: String,
        orderQuantity: Long
    ): OptionWithProductView {
        try {
            val itemInformation =
                this.productClient.getOptionWithProduct(
                    productId = productId,
                    optionId = optionId
                ).body
                    ?: throw NotFoundOptionException()
            require(itemInformation.stock >= orderQuantity) { throw CartStockException(optionId = optionId) }
            return itemInformation
        } catch (e: Exception) {
            log.error { "error:::${e}" }
            throw CartProductFeignException("product api call fail!")
        }
    }

    private fun validateUser(userId: Long) {
        try {
            this.baseUserComponent.validateUserByUserId(userId = userId)
        } catch (e: Exception) {
            throw CartInvalidException(e.message)
        }
    }

    @Transactional
    override fun deleteItem(cartItemId: Long) =
        this.cartRepository.delete(
            this.cartRepository.findById(cartItemId).orElseThrow { NotFoundCartItemException(cartItemId = cartItemId) })

    override fun getItemListByUserId(userId: Long): List<CartResponseView> {
        val list = this.cartRepository.findAllByUserId(userId)
        return list.map { item -> CartResponseView.makeView(item) }.toList()
    }
    // this.cartRepository.findAllByUserId(userId).stream().map { item -> CartResponseView.makeView(item) }.toList()

    @Transactional
    override fun updateOrderQuantity(cartItemId: Long, orderQuantity: Long): CartResponseView {
        val cartItem = this.cartRepository.findById(cartItemId).orElseThrow { NotFoundCartItemException(cartItemId = cartItemId) }

        val availableStock =
            this.productClient.getOption(cartItem.optionId).body?.stock ?: throw NotFoundOptionException()
        require(availableStock >= orderQuantity) { throw CartStockException(optionId = cartItem.optionId) }

        cartItem.updateOrderQuantity(quantity = orderQuantity)
        return CartResponseView.makeView(this.cartRepository.save(cartItem))
    }
}