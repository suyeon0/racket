package com.racket.share.domain.cash.entity

import com.racket.share.domain.cash.enums.CashEventType
import com.racket.share.domain.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.persistence.*

@Document("cashTransaction")
class CashTransaction(

    @Id
    @Column(name = "id", nullable = false)
    var id: ObjectId? = null,

    @Column(name = "transaction_id", nullable = false)
    var transactionId: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "amount", nullable = false)
    val amount: Long,

    @Column(name = "event_type", nullable = false)
    val eventType: CashEventType,

    @Column(name = "account_id", nullable = false)
    val accountId: Long,

    @Column(name = "status")
    val status: CashTransactionStatusType

)