package com.racket.cash

import com.racket.cash.request.CreateChargeCommand
import com.racket.cash.request.CompleteCashChargeCommand
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/racket-cash")
@Tag(name = "RacketCash-API", description = "캐시 충전 및 사용을 담당")
class CashController(
    private val cashService: CashService
)  {
    @PostMapping("/charge")
    fun postToCharge(@RequestBody chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView> {
        this.validateToCharge(chargeCommand)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                this.cashService.requestCharge(
                    CashService.ChargeDTO(
                        userId = chargeCommand.userId,
                        amount = chargeCommand.amount,
                        accountId = chargeCommand.accountId
                    )
                )
            )
    }

    private fun validateToCharge(chargeCommand: CreateChargeCommand) {
        // 1. userId 유효 여부 확인
        //this.cashUserService.validateUserId(userId = chargeCommand.userId)

        // 2. Amount - 충전 단위
        val validChargeUnitSet = this.cashService.getChargeUnitSet()
        chargeCommand.validate(validChargeUnitSet)

        // 3. 계좌정보 유효 여부 확인
        //this.cashUserService.validateUserId(chargeCommand.userId)
    }

    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(@PathVariable userId: Long) = ResponseEntity.ok().body(this.cashService.getBalanceByUserId(userId))

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CashTransactionResponseView> {
        return ResponseEntity.ok().body(this.cashService.getTransactionById(ObjectId(transactionId)))
    }

    @GetMapping("/transactions")
    fun completeCharge(completeCashChargeCommand: CompleteCashChargeCommand): ResponseEntity<CashBalanceResponseView> {
        return ResponseEntity.ok().body(
            this.cashService.completeCharge(
                CashService.ChargeDTO(
                    userId = completeCashChargeCommand.userId,
                    amount = completeCashChargeCommand.amount,
                    transactionId = completeCashChargeCommand.transactionId,
                    accountId = completeCashChargeCommand.accountId,
                )
            )
        )
    }
}