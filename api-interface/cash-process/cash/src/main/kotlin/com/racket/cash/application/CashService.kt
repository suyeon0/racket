package com.racket.cash.application

import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import com.racket.cash.presentation.response.WithdrawAccountResponseView
import org.bson.types.ObjectId

interface CashService {

    fun charge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

    fun getWithdrawAccountListByUserId(userId: Long): List<WithdrawAccountResponseView>

    fun getTransactionById(transactionId: ObjectId): CashTransactionResponseView

    fun updateBalance(userId: Long, amount: Long): CashBalanceResponseView
    fun getChargeUnitSet(): Set<Long>

    data class ChargeDTO (
        val userId: Long,
        val amount: Long,
        val accountId: Long
    )
}