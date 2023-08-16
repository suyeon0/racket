package com.racket.cash.application

import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import com.racket.cash.presentation.response.CashBalanceResponseView
import com.racket.cash.presentation.response.CashTransactionResponseView
import com.racket.cash.presentation.response.ChargeResponseView
import com.racket.cash.presentation.response.WithdrawAccountResponseView
import org.bson.types.ObjectId
import java.util.*

interface CashService {

    fun requestCharge(chargeDTO: ChargeDTO): ChargeResponseView

    fun getBalanceByUserId(userId: Long): CashBalanceResponseView

    fun getWithdrawAccountListByUserId(userId: Long): List<WithdrawAccountResponseView>

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