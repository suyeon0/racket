package com.racket.api.cash.request

import com.racket.share.domain.cash.enums.CashEventType
import com.racket.share.domain.cash.enums.CashTransactionStatusType

sealed class CashChargeCommand {
    data class Success(
        val transactionId: String,
        val userId: Long,
        val amount: Long,
        val accountId: Long,
        val eventType: CashEventType,
        val status: CashTransactionStatusType = CashTransactionStatusType.COMPLETED
    ) : CashChargeCommand()

    data class Failure(
        val transactionId: String,
        val userId: Long,
        val amount: Long,
        val accountId: Long,
        val eventType: CashEventType,
        val status: CashTransactionStatusType = CashTransactionStatusType.FAILED
    ) : CashChargeCommand()

    data class Request(
        val transactionId: String,
        val userId: Long,
        val amount: Long,
        val accountId: Long,
        val eventType: CashEventType,
        val status: CashTransactionStatusType = CashTransactionStatusType.REQUEST
    ) : CashChargeCommand()
}