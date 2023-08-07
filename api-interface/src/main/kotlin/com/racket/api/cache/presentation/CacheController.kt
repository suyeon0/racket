package com.racket.api.cache.presentation

import com.racket.api.cache.CacheService
import com.racket.api.cache.CacheUserService
import com.racket.api.cache.presentation.reponse.ChargeResponseView
import com.racket.api.cache.presentation.request.CreateChargeCommand
import com.racket.api.shared.annotation.LongTypeIdInputValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/racket-cache")
class CacheController(
    private val cacheService: CacheService,
    private val cacheUserService: CacheUserService
) : CacheSpecification {
    override fun postToCharge(chargeCommand: CreateChargeCommand): ResponseEntity<ChargeResponseView> {
        this.ValidateToCharge(chargeCommand)

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

    private fun ValidateToCharge(chargeCommand: CreateChargeCommand) {
        chargeCommand.validate()
        this.cacheUserService.validateUserId(chargeCommand.userId)
    }

    @LongTypeIdInputValidator
    override fun getBalanceByUserId(userId: Long) =
        ResponseEntity.ok().body(this.cacheService.getBalanceByUserId(userId))

    override fun getTransactionList() {
        TODO("Not yet implemented")
    }

    override fun postToUse() {
        TODO("Not yet implemented")
    }
}