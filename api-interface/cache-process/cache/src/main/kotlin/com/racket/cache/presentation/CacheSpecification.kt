package com.racket.cache.presentation

import com.racket.cache.presentation.request.CreateChargeCommand
import com.racket.cache.presentation.request.UpdateBalanceCommand
import com.racket.cache.presentation.response.CacheBalanceResponseView
import com.racket.cache.presentation.response.CacheTransactionResponseView
import com.racket.cache.presentation.response.ChargeResponseView
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "RacketCache-API", description = "캐시 충전 및 사용을 담당")
interface CacheSpecification {

    @PostMapping("/charge")
    fun postToCharge(@RequestBody chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView>

    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(userId: Long): ResponseEntity<CacheBalanceResponseView>

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CacheTransactionResponseView>

    @GetMapping("/transactions")
    fun getTransactionList()

    @PostMapping("/use")
    fun postToUse()

    @PostMapping("/balance")
    fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CacheBalanceResponseView>

}