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

    @Column(name = "account_id", nullable = false)
    val accountId: Long,

    @Column(name = "status")
    val status: CashTransactionStatusType

) {

    // 충전이 불가능한 트랜잭션 조건
    fun isImpossibleTransactionToCharge() =
        this.status == CashTransactionStatusType.COMPLETED || this.eventType != CashEventType.CHARGING

}