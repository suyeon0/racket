package com.racket.cache.presentation

import com.racket.cache.presentation.request.CreateChargeCommand
import com.racket.api.shared.annotation.LongTypeIdInputValidator
import com.racket.cache.application.CacheService
import com.racket.cache.application.CacheUserService
import com.racket.cache.presentation.request.UpdateBalanceCommand
import com.racket.cache.presentation.response.CacheBalanceResponseView
import com.racket.cache.presentation.response.CacheTransactionResponseView
import com.racket.cache.presentation.response.ChargeResponseView
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/racket-cache")
class CacheController(
    private val cacheService: CacheService,
    private val cacheUserService: CacheUserService
) : CacheSpecification {
    override fun postToCharge(chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView> {
        this.validateToCharge(chargeCommand)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                this.cacheService.charge(
                    CacheService.ChargeDTO(
                        userId = chargeCommand.userId,
                        amount = chargeCommand.amount,
                        chargingWayId = chargeCommand.userChargingWayId
                    )
                )
            )
    }

    private fun validateToCharge(chargeCommand: CreateChargeCommand) {
        chargeCommand.validate()
        this.cacheUserService.validateUserId(chargeCommand.userId)
    }

    @LongTypeIdInputValidator
    override fun getBalanceByUserId(userId: Long) = ResponseEntity.ok().body(this.cacheService.getBalanceByUserId(userId))

    override fun getTransaction(@PathVariable transactionId: String): ResponseEntity<CacheTransactionResponseView> {
        return ResponseEntity.ok().body(this.cacheService.getTransactionById(ObjectId(transactionId)))
    }

    override fun postToUpdateBalance(@RequestBody updateBalanceCommand: UpdateBalanceCommand): ResponseEntity<CacheBalanceResponseView> {
        updateBalanceCommand.validate()
        return ResponseEntity.ok().body(
            this.cacheService.updateBalance(
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