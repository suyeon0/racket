package com.racket.cash.presentation

import com.racket.cash.presentation.request.CreateChargeCommand
import com.racket.cash.presentation.request.UpdateBalanceCommand
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "RacketCash-API", description = "캐시 충전 및 사용을 담당")
interface CashSpecification {

    @PostMapping("/charge")
    fun postToCharge(@RequestBody chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView>

    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(userId: Long): ResponseEntity<CashBalanceResponseView>

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CashTransactionResponseView>

    @GetMapping("/transactions")
    fun getTransactionList()

    @PostMapping("/use")
    fun postToUse()

    @PostMapping("/balance")
    fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CashBalanceResponseView>

}