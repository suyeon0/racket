package com.racket.api.cart.annotation

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "장바구니 API")
@RequestMapping("/api/v1/cart")
annotation class CartApiV1