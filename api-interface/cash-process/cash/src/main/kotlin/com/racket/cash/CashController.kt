package com.racket.cash

import com.racket.api.shared.payment.BasePaymentComponent
import com.racket.api.shared.user.BaseUserComponent
import com.racket.cash.exception.InvalidChargingTransactionException
import com.racket.cash.request.CashChargeCommand
import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.ChargeResponseView
import com.racket.cash.vo.ChargeVO
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
    @PostMapping("/charge/request")
    fun postToCharge(@RequestBody chargeCommand: CashChargeCommand): ResponseEntity<ChargeResponseView> {
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

    private fun validateToCharge(chargeCommand: CashChargeCommand) {
        try {
            // 1. Amount - 충전 단위
            val validChargeUnitSet = this.cashService.getChargeUnitSet()
            chargeCommand.validate(validChargeUnitSet)

            // 2. userId 유효 여부 확인
            this.baseUserComponent.validateUserByUserId(userId = chargeCommand.userId)

            // 3. 계좌정보 유효 여부 확인
            this.basePaymentComponent.validateAccount(accountId = chargeCommand.accountId)
        } catch (e: Exception) {
            throw InvalidChargingTransactionException(message = e.message!!)
        }
    }

    @PostMapping("/charge/complete")
    fun completeCharge(@RequestBody chargeCommand: CashChargeCommand): ResponseEntity<CashBalanceResponseView> {
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

    @GetMapping("/balance/{userId}")
    fun getBalanceByUserId(@PathVariable userId: Long) = ResponseEntity.ok().body(this.cashService.getBalanceByUserId(userId))

}