package com.racket.cash.presentation.response

import java.util.*

data class CashTransactionResponseView(

    val id: String,
    val userId: Long,
    val amount: Long,
    val createdAt: Date

)