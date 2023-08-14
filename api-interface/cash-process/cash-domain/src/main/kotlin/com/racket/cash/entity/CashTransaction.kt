package com.racket.cash.entity

import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Document("cashTransaction")
class CashTransaction(

    @Id
    @Column(name = "id", nullable = false)
    var id: ObjectId? = null,

    @Column(name = "transaction_id", nullable = false)
    var transactionId: ObjectId,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "amount", nullable = false)
    val amount: Long,

    @Column(name = "event_type", nullable = false)
    val eventType: CashEventType,

    @Column(name = "account_no", nullable = false)
    val accountNo: Long,

    @Column(name = "status")
    val status: CashTransactionStatusType

)