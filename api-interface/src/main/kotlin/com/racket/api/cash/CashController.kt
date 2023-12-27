package com.racket.api.cash

import com.racket.api.shared.annotation.SwaggerFailResponse
import com.racket.api.shared.payment.BasePaymentComponent
import com.racket.api.shared.user.BaseUserComponent
import com.racket.api.cash.request.CashChargeCommand
import com.racket.api.cash.response.CashBalanceResponseView
import com.racket.api.cash.response.ChargeResponseView
import com.racket.api.cash.vo.ChargeVO
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import com.racket.share.domain.cash.exception.InvalidChargingTransactionException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/racket-cash")
@Tag(name = "RacketCash-API", description = "캐시 충전/사용, 총액 조회")
class CashController(
    private val cashService: CashService,
    private val baseUserComponent: BaseUserComponent,
    private val basePaymentComponent: BasePaymentComponent
)  {

    @SwaggerFailResponse
    @Operation(
        summary = "캐시 충전 요청",
        description = "캐시 충전 요청",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Success",
                content = [Content(schema = Schema(implementation = ChargeResponseView::class))]
            )
        ]
    )
    @PostMapping("/charge/request")
    fun postToCharge(@RequestBody chargeCommand: CashChargeCommand.Request): ResponseEntity<ChargeResponseView> {
        this.validateToCharge(chargeCommand)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                this.cashService.requestCharge(
                    ChargeVO(
                        userId = chargeCommand.userId,
                        amount = chargeCommand.amount,
                        accountId = chargeCommand.accountId,
                        eventType = chargeCommand.eventType,
                        status = chargeCommand.status
                    )
                )
            )
    }

    private fun validateToCharge(chargeCommand: CashChargeCommand.Request) {
        try {
            // 1. Amount - 충전 단위
            val validChargeUnitSet = this.cashService.getChargeUnitSet()
            // TODO!
            //chargeCommand.validate(validChargeUnitSet)

            // 2. userId 유효 여부 확인
            this.baseUserComponent.validateUserByUserId(userId = chargeCommand.userId)

            // 3. 계좌정보 유효 여부 확인
            this.basePaymentComponent.validateAccount(accountId = chargeCommand.accountId)
        } catch (e: Exception) {
            throw InvalidChargingTransactionException(message = e.message!!)
        }
    }


    @SwaggerFailResponse
    @Operation(
        summary = "캐시 충전 DB 반영",
        description = "캐시 충전 DB 반영",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CashBalanceResponseView::class))]
            )
        ]
    )
    @PostMapping("/charge/complete")
    fun completeCharge(@RequestBody chargeCommand: CashChargeCommand.Success): ResponseEntity<CashBalanceResponseView> {
        return ResponseEntity.ok().body(
            this.cashService.completeCharge(
                ChargeVO(
                    transactionId = chargeCommand.transactionId,
                    userId = chargeCommand.userId,
                    amount = chargeCommand.amount,
                    accountId = chargeCommand.accountId,
                    eventType = chargeCommand.eventType,
                    status = chargeCommand.status
                )
            )
        )
    }

    @SwaggerFailResponse
    @Operation(
        summary = "캐시 조회",
        description = "캐시 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = CashBalanceResponseView::class))]
            )
        ]
    )
    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(@PathVariable userId: Long) = ResponseEntity.ok().body(this.cashService.getBalanceByUserId(userId))

}