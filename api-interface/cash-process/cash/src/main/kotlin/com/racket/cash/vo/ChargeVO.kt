package com.racket.cash.vo

import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId

data class ChargeVO(

    var transactionId: ObjectId? = null,
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
