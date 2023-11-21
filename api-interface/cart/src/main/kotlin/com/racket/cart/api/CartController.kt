package com.racket.cart.api

import com.racket.api.product.presentation.response.ProductResponseView
import com.racket.api.shared.annotation.SwaggerFailResponse
import com.racket.cart.api.annotation.CartApiV1
import com.racket.cart.api.request.CartCreateRequestCommand
import com.racket.cart.api.request.CartUpdateRequestCommand
import com.racket.cart.api.response.CartResponseView
import com.racket.cart.api.vo.CartItemRequestVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
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
    @SwaggerFailResponse
    @Operation(
        summary = "장바구니 추가",
        description = "장바구니 추가",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = CartResponseView::class))]
            )
        ]
    )
    fun postItem(@RequestBody request: CartCreateRequestCommand): ResponseEntity<CartResponseView> {
        request.validate()

        return ResponseEntity.status(HttpStatus.CREATED).body(
            this.cartService.addItem(
                item = CartItemRequestVO(
                    userId = request.userId!!,
                    productId = request.productId!!,
                    optionId = request.optionId!!,
                    originalPrice = request.originalPrice!!,
                    calculatedPrice = request.calculatedPrice!!,
                    orderQuantity = request.orderQuantity!!
                )
            )
        )
    }

    @SwaggerFailResponse
    @Operation(
        summary = "장바구니 아이템 수량 변경",
        description = "장바구니 아이템 수량 변경",
        parameters = [Parameter(name = "cartItemId", description = "장바구니 아이템 ID", example = "40")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CartResponseView::class))]
            )
        ]
    )
    @PatchMapping("/{cartItemId}/quantity")
    fun updateItemQuantity(
        @PathVariable cartItemId: Long,
        @RequestBody request: CartUpdateRequestCommand
    ): ResponseEntity<CartResponseView> {
        request.validate()
        return ResponseEntity.ok(
            this.cartService.updateOrderQuantity(
                cartItemId = cartItemId,
                orderQuantity = request.orderQuantity
            )
        )
    }

    @SwaggerFailResponse
    @Operation(
        summary = "유저의 장바구니 조회",
        description = "유저의 장바구니 아이템 리스트 조회",
        parameters = [Parameter(name = "userId", description = "유저 ID", example = "28")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CartResponseView::class))]
            )
        ]
    )
    @GetMapping("/{userId}")
    fun getItemList(@PathVariable userId: Long) =
        ResponseEntity.ok(this.cartService.getItemListByUserId(userId))


    @SwaggerFailResponse
    @Operation(
        summary = "장바구니 아이템 삭제",
        description = "장바구니 아이템 삭제",
        parameters = [Parameter(name = "cartItemId", description = "장바구니 아이템 ID")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CartResponseView::class))]
            )
        ]
    )
    @DeleteMapping("/{cartItemId}")
    fun deleteItem(@PathVariable cartItemId: Long): ResponseEntity<Unit> {
        // TODO: user cart item 확인
        return ResponseEntity.ok(this.cartService.deleteItem(cartItemId))
    }

}