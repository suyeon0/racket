package com.racket.cash.response

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.racket.cash.entity.CashTransaction
import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId
import util.ObjectIdDeserializer
import util.ObjectIdSerializer
import java.time.LocalDateTime
import java.util.*

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