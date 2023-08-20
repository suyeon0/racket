package com.racket.cash.response

import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId
import java.util.*

data class CashTransactionResponseView(

    val id: String,
    val transactionId: String,
    val userId: Long,
    val amount: Long,
    val createdAt: Date,
    val status: CashTransactionStatusType,
    val accountId: Long,
    val eventType: CashEventType

)

fun CashTransactionResponseView.makeCashTransactionResponseToEntity(): CashTransaction {
    return CashTransaction(
        id = ObjectId(id),
        transactionId = ObjectId(transactionId),
        status = status,
        userId = userId,
        amount = amount,
        accountId = accountId,
        eventType = eventType
    )
}