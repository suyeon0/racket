package com.racket.cache.application

import com.racket.cache.entity.UserChargingWayInfo
import com.racket.cache.presentation.response.CacheBalanceResponseView
import com.racket.cache.presentation.response.CacheTransactionResponseView
import com.racket.cache.presentation.response.ChargeResponseView
import org.bson.types.ObjectId

interface CacheService {

    fun charge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CacheBalanceResponseView

    fun getChargingWayById(userId: Long): UserChargingWayInfo

    fun getTransactionById(transactionId: ObjectId): CacheTransactionResponseView

    fun updateBalance(userId: Long, amount: Long): CacheBalanceResponseView

    data class ChargeDTO (
        val userId: Long,
        val amount: Long,
        val chargingWayId: Long
    )
}