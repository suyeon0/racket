package com.racket.cash

import com.racket.cash.request.CashChargeCommand
import com.racket.cash.vo.ChargeVO
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/racket-cash/transaction")
@Tag(name = "RacketCash-API", description = "캐시 충전/사용 내역 조회")
class CashTransactionController(
    private val cashTransactionLogService: CashTransactionLogService
) {

    @GetMapping("/{transactionId}")
    fun getTransaction(@PathVariable transactionId: ObjectId) =
        ResponseEntity.ok().body(this.cashTransactionLogService.getTransactionListByTransactionId(transactionId))

    @PostMapping
    fun postTransaction(@RequestBody transactionCommand: CashChargeCommand) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(
                this.cashTransactionLogService.insertChargeTransaction(
                    ChargeVO(
                        transactionId = transactionCommand.transactionId,
                        userId = transactionCommand.userId,
                        amount = transactionCommand.amount,
                        accountId = transactionCommand.accountId,
                        eventType = transactionCommand.eventType,
                        status = transactionCommand.status
                    )
                )
            )
}