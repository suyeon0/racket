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
        val transactionId: String?,
        val userId: Long,
        val amount: Long,
        val accountId: Long,
        val eventType: CashEventType,
        val status: CashTransactionStatusType
    ) : CashChargeCommand() {
        fun validate(validChargeUnitSet: Set<Long>) {
            // 충전 단위 체크
            if (!validChargeUnitSet.contains(this.amount)) {
                throw IllegalArgumentException("Invalid Charge Unit!")
            }

            // request 단계가 아니라면 transactionID가 있어야 한다
            if (status != CashTransactionStatusType.REQUEST) {
                if (transactionId == null) throw IllegalArgumentException("transactionId must be not null")
            }
        }
    }
}