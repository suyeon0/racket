package com.racket.cart.api.client.product

import com.racket.api.product.option.response.OptionResponseView
import com.racket.api.product.option.response.OptionWithProductView
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "product", url = "\${client.serviceUrl.product}", contextId = "cart-product")
interface ProductClient {

    @GetMapping("/option/{optionId}")
    fun getOption(@PathVariable optionId: String): ResponseEntity<OptionResponseView>

    @GetMapping("/option-product")
    fun getOptionWithProduct(@RequestParam productId: String, @RequestParam optionId: String): ResponseEntity<OptionWithProductView>

}