package com.racket.api.cash.vo

import com.racket.share.domain.cash.entity.CashTransaction
import com.racket.share.domain.cash.enums.CashEventType
import com.racket.share.domain.cash.enums.CashTransactionStatusType

data class ChargeVO(

    var transactionId: String? = null,
    val userId: Long,
    val amount: Long,
    val accountId: Long,
    val eventType: CashEventType,
    val status: CashTransactionStatusType

)

fun ChargeVO.makeCashTransactionEntity() =
    CashTransaction(
        transactionId = transactionId!!,
        userId = userId,
        amount = amount,
        eventType = eventType,
        accountId = accountId,
        status = status
    )
