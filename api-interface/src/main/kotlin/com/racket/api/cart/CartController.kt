package com.racket.api.cart

import com.racket.api.cart.annotation.CartApiV1
import com.racket.api.cart.request.CartCreateRequestCommand
import com.racket.api.cart.request.CartUpdateRequestCommand
import com.racket.api.cart.response.CartResponseView
import com.racket.api.cart.vo.CartItemRequestVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@CartApiV1
class CartController(
    private val cartService: CartService
) {

    @PostMapping
    fun postItem(@RequestBody request: CartCreateRequestCommand): ResponseEntity<CartResponseView> {
        request.validate()
        return ResponseEntity.ok(
            this.cartService.addItem(
                item = CartItemRequestVO(
                    userId = request.userId,
                    productId = request.productId,
                    optionId = request.optionId,
                    optionName = request.optionName,
                    originalPrice = request.originalPrice,
                    calculatedPrice = request.calculatedPrice,
                    orderQuantity = request.orderQuantity,
                    deliveryInformation = request.deliveryInformation
                )
            )
        )
    }

    @PatchMapping("/{cartItemId}/quantity")
    fun updateItemQuantity(@PathVariable cartItemId: Long, @RequestBody request: CartUpdateRequestCommand): ResponseEntity<CartResponseView> {
        request.validate()
        return ResponseEntity.ok(this.cartService.updateOrderQuantity(cartItemId = cartItemId, orderQuantity = request.orderQuantity))
    }

    @GetMapping("/{userId}")
    fun getItemList(@PathVariable userId: Long) =
        ResponseEntity.ok(this.cartService.getItemListByUserId(userId = userId))

    @DeleteMapping("/{cartItemId}")
    fun deleteItem(@PathVariable cartItemId: Long): ResponseEntity<Unit> {
        // TODO: user cart item 확인
        return ResponseEntity.ok(this.cartService.deleteItem(cartItemId = cartItemId))
    }

}