package com.racket.api.cart

import com.racket.api.auth.login.session.SessionRedisManager
import com.racket.api.cart.annotation.CartApiV1
import com.racket.api.cart.request.CartCreateRequestCommand
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
    fun postItem(@RequestBody requestCommand: CartCreateRequestCommand): ResponseEntity<Unit> {
        requestCommand.validate()
        return ResponseEntity.ok(this.cartService.addItem(userId = requestCommand.userId, itemList = requestCommand.itemList))
    }

    @PatchMapping("/quantity/{cartItemId}/{quantity}")
    fun updateItemQuantity(@PathVariable cartItemId: Long, @PathVariable quantity: Long): ResponseEntity<Unit> {
        if(quantity <= 0) throw IllegalArgumentException()
        return ResponseEntity.ok(this.cartService.updateOrderQuantity(cartItemId = cartItemId, quantity = quantity))
    }

    @GetMapping("/{user_id}")
    fun getItemList(@PathVariable userId: Long) {

    }

    @DeleteMapping("/{cartItemId}")
    fun deleteItem(@PathVariable cartItemId: Long): ResponseEntity<Unit> {
        // TODO: user cart item 확인
        return ResponseEntity.ok(this.cartService.deleteItem())
    }


    @GetMapping
    fun getCartItemCount() {

    }


}