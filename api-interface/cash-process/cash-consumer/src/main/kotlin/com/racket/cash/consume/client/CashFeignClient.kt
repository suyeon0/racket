package com.racket.cash.consume.client

import com.racket.cash.presentation.request.UpdateBalanceCommand
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "cash", url = "\${client.serviceUrl.cash}")
interface CashFeignClient {

    /**
     * Consumer - Cache Charging Process 처리
     */
    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CashTransactionResponseView>

    @PostMapping("/balance")
    fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CashBalanceResponseView>
}