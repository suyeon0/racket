package com.racket.cash.presentation.request

import com.racket.cash.enums.CashEventType
import com.racket.cash.enums.CashTransactionStatusType

data class CompleteCashChargeCommand (

    val userId :Long,
    val amount : Long,
    val transactionId: String,
    val status: CashTransactionStatusType = CashTransactionStatusType.COMPLETED,
    val accountId: Long,
    val eventType: CashEventType

)