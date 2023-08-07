package com.racket.api.cache

import com.racket.api.cache.domain.entity.UserChargingWayInfo
import com.racket.api.cache.presentation.reponse.CacheBalanceResponseView
import com.racket.api.cache.presentation.reponse.ChargeResponseView
import java.util.*

interface CacheService {

    fun charge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CacheBalanceResponseView

    fun getChargingWayById(userId: Long): UserChargingWayInfo

    data class ChargeDTO (
        val userId: Long,
        val amount: Long,
        val chargingWayId: Long
    )
}