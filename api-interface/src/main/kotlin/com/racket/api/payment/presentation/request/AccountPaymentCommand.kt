package com.racket.api.payment.presentation.request

data class AccountPaymentCommand(
    val accountId: Long,
    val amount: Long
)