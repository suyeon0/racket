package com.racket.cash.consume.client

import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.cash.response.CashBalanceResponseView
import com.racket.api.cash.response.CashTransactionResponseView
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "cash", url = "\${client.serviceUrl.cash}")
interface CashFeignClient {

    /**
     * Consumer - Cache Charging Process 처리
     */
    @GetMapping("/transaction/list/{transactionId}")
    fun getTransactionList(@PathVariable transactionId: String): ResponseEntity<List<CashTransactionResponseView>>

    @PostMapping("/charge/complete")
    fun completeCharge(@RequestBody chargeCommand: CashChargeCommand): ResponseEntity<CashBalanceResponseView>

    @PostMapping("/transaction")
    fun postTransaction(@RequestBody transactionCommand: CashChargeCommand)
}