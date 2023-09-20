package com.racket.delivery

import com.racket.delivery.annotation.DeliveryApiV1
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@DeliveryApiV1
class DeliveryController(
    private val deliveryService: DeliveryService
) {

    @GetMapping("/{optionId}")
    fun get(@PathVariable optionId: Long) = ResponseEntity.ok(this.deliveryService.get(optionId = optionId))

}