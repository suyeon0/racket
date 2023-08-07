package com.racket.api.cache.presentation

import com.racket.api.cache.presentation.reponse.CacheBalanceResponseView
import com.racket.api.cache.presentation.reponse.ChargeResponseView
import com.racket.api.cache.presentation.request.CreateChargeCommand
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "RacketCache-API", description = "캐시 충전 및 사용을 담당")
interface CacheSpecification {

    @PostMapping("/charge")
    fun postToCharge(@RequestBody chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView>

    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(userId: Long): ResponseEntity<CacheBalanceResponseView>

    @GetMapping("/transactions")
    fun getTransactionList()

    @PostMapping("/use")
    fun postToUse()

}