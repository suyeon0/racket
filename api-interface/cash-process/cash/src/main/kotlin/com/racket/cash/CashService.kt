package com.racket.cash

import com.racket.cash.response.CashBalanceResponseView
import com.racket.cash.response.CashTransactionResponseView
import com.racket.cash.response.ChargeResponseView
import org.bson.types.ObjectId

interface CashService {

    fun requestCharge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

    fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView

    fun completeCharge(chargeDTO: ChargeDTO): CashBalanceResponseView

    fun getChargeUnitSet(): Set<Long>

    data class ChargeDTO (
        val userId: Long,
        val amount: Long,
        var transactionId: String? = null,
        val accountId: Long,
    )
}