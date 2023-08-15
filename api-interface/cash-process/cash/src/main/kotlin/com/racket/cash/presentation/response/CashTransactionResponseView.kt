package com.racket.cash.presentation.response

import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType
import java.util.*

data class CashTransactionResponseView(

    val id: String,
    val transactionId: String,
    val userId: Long,
    val amount: Long,
    val createdAt: Date,
    val status: CashTransactionStatusType,
    val accountNo: Long,
    val eventType: CashEventType

)