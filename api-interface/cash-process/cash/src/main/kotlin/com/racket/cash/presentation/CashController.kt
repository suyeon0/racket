package com.racket.cash.presentation

import com.racket.cash.presentation.request.CreateChargeCommand
import com.racket.cash.application.CashService
import com.racket.cash.application.CashUserService
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.presentation.request.CompleteCashChargeCommand
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import com.racket.cash.presentation.response.WithdrawAccountResponseView
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/racket-cash")
class CashController(
    private val cashService: CashService,
    private val cashUserService: CashUserService
) : CashSpecification {
    override fun getWithdrawAccountList(userId: Long): ResponseEntity<List<WithdrawAccountResponseView>> =
        ResponseEntity.ok()
            .body(this.cashService.getWithdrawAccountListByUserId(userId = userId))

    override fun postToCharge(chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView> {
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
        this.cashUserService.validateUserId(userId = chargeCommand.userId)

        // 2. Amount - 충전 단위
        val validChargeUnitSet = this.cashService.getChargeUnitSet()
        chargeCommand.validate(validChargeUnitSet)

        // 3. 계좌정보 유효 여부 확인
        this.cashUserService.validateUserId(chargeCommand.userId)
    }

    override fun getBalanceByUserId(userId: Long) = ResponseEntity.ok().body(this.cashService.getBalanceByUserId(userId))

    override fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CashTransactionResponseView> {
        return ResponseEntity.ok().body(this.cashService.getTransactionById(ObjectId(transactionId)))
    }

    override fun completeCharge(completeCashChargeCommand: CompleteCashChargeCommand): ResponseEntity<CashBalanceResponseView> {
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

    override fun getTransactionList() {
        TODO("Not yet implemented")
    }

    override fun postToUse() {
        TODO("Not yet implemented")
    }


}