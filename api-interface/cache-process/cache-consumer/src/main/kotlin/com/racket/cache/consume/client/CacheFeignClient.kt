package com.racket.cache.consume.client

import com.racket.cache.presentation.request.UpdateBalanceCommand
import com.racket.cache.presentation.response.CacheBalanceResponseView
import com.racket.cache.presentation.response.CacheTransactionResponseView
import org.bson.types.ObjectId
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(name = "cache", url = "\${client.serviceUrl.cache}")
interface CacheFeignClient {

    /**
     * Consumer - Cache Charging Process 처리
     */
    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CacheTransactionResponseView>

    @PostMapping("/balance")
    fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CacheBalanceResponseView>
}