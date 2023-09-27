package com.racket.cart.api.client.product

import com.racket.api.product.option.reponse.OptionResponseView
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "product", url = "\${client.serviceUrl.product}", contextId = "cart-product")
interface ProductClient {

    @GetMapping("/option/{optionId}")
    fun getOption(@PathVariable optionId: Long): ResponseEntity<OptionResponseView>

}