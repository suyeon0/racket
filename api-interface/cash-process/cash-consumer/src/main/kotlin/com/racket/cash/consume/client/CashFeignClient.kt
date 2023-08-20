package com.racket.cash.consume.client

import com.racket.cash.request.CompleteCashChargeCommand
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
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

    @PostMapping("/charge/complete")
    fun completeCharge(@RequestBody completeCashChargeCommand: CompleteCashChargeCommand): ResponseEntity<CashBalanceResponseView>
}