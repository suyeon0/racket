package com.racket.cash.presentation

import com.racket.cash.presentation.request.CreateChargeCommand
import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.cash.application.CashService
import com.racket.cash.application.CashUserService
import com.racket.cash.presentation.request.UpdateBalanceCommand
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/racket-cash")
class CashController(
    private val cashService: CashService,
    private val cashUserService: CashUserService
) : CashSpecification {
    override fun postToCharge(chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView> {
        this.validateToCharge(chargeCommand)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                this.cashService.charge(
                    CashService.ChargeDTO(
                        userId = chargeCommand.userId,
                        amount = chargeCommand.amount,
                        chargingWayId = chargeCommand.userChargingWayId
                    )
                )
            )
    }

    private fun validateToCharge(chargeCommand: CreateChargeCommand) {
        chargeCommand.validate()
        this.cashUserService.validateUserId(chargeCommand.userId)
    }

    @LongTypeIdInputValidator
    override fun getBalanceByUserId(userId: Long) = ResponseEntity.ok().body(this.cashService.getBalanceByUserId(userId))

    override fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CashTransactionResponseView> {
        return ResponseEntity.ok().body(this.cashService.getTransactionById(ObjectId(transactionId)))
    }

    override fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CashBalanceResponseView> {
        updateBalanceCommand.validate()
        return ResponseEntity.ok().body(
            this.cashService.updateBalance(
                userId = updateBalanceCommand.userId,
                amount = updateBalanceCommand.amount
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