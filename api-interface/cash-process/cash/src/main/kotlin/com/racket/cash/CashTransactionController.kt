package com.racket.cash

import com.racket.api.shared.annotation.SwaggerFailResponse
import com.racket.cash.entity.CashTransaction
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.cash.vo.ChargeVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

    @SwaggerFailResponse
    @Operation(
        summary = "캐시 충전 내역 리스트 조회",
        description = "캐시 충전 내역 리스트 조회",
        parameters = [Parameter(name = "transactionId", description = "충전 요청 트랜잭션 ID", example = "")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CashTransactionResponseView::class))]
            )
        ]
    )
    @GetMapping("/list/{transactionId}")
    fun getTransactionListByTransactionId(@PathVariable transactionId: String) =
        ResponseEntity.ok().body(this.cashTransactionLogService.getTransactionListByTransactionId(transactionId))



    @SwaggerFailResponse
    @Operation(
        summary = "캐시 충전 내역 단건 조회",
        description = "캐시 충전 내역 단건 조회",
        parameters = [Parameter(name = "eventId", description = "내역 ID", example = "")],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CashTransactionResponseView::class))]
            )
        ]
    )
    @GetMapping("/{eventId}")
    fun getTransaction(@PathVariable eventId: ObjectId) =
        ResponseEntity.ok().body(this.cashTransactionLogService.getTransactionById(eventId))


    @SwaggerFailResponse
    @Operation(
        summary = "캐시 충전 내역 생성",
        description = "캐시 충전 내역 생성",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = CashTransaction::class))]
            )
        ]
    )
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