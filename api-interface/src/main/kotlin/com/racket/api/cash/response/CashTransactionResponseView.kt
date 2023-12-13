package com.racket.api.cash.response

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.racket.share.domain.cash.entity.CashTransaction
import com.racket.share.domain.cash.enums.CashEventType
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId
import util.ObjectIdDeserializer
import util.ObjectIdSerializer

data class CashTransactionResponseView(

    @JsonSerialize(using = ObjectIdSerializer::class)
    @JsonDeserialize(using = ObjectIdDeserializer::class)
    val id: ObjectId,
    val transactionId: String,
    val userId: Long,
    val amount: Long,
    val status: CashTransactionStatusType,
    val accountId: Long,
    val eventType: CashEventType

)

fun CashTransactionResponseView.makeCashTransactionResponseToEntity(): CashTransaction {
    return CashTransaction(
        id = id,
        transactionId = transactionId,
        status = status,
        userId = userId,
        amount = amount,
        accountId = accountId,
        eventType = eventType
    )
}