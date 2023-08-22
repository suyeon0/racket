package com.racket.cash

import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/racket-cash/transaction")
@Tag(name = "RacketCash-API", description = "캐시 충전/사용 내역 조회")
class CashTransactionController(
    private val cashTransactionLogService: CashTransactionLogService
) {

    @GetMapping("/{transactionId}")
    fun getTransaction(@PathVariable transactionId: ObjectId) =
        ResponseEntity.ok().body(this.cashTransactionLogService.getTransactionListByTransactionId(transactionId))

}