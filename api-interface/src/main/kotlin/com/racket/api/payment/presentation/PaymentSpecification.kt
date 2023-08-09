package com.racket.api.payment.presentation

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


interface PaymentSpecification {

    @Operation(summary = "결제")
    @PostMapping
    fun pay(@RequestBody request: Long): ResponseEntity<String>
}