package com.racket.cash.application

import com.racket.cash.entity.UserChargingWayInfo
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import org.bson.types.ObjectId

interface CashService {

    fun charge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

    fun getChargingWayById(userId: Long): UserChargingWayInfo

    fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView

    fun updateBalance(userId: Long, amount: Long): CashBalanceResponseView

    data class ChargeDTO (
        val userId: Long,
        val amount: Long,
        val chargingWayId: Long
    )
}