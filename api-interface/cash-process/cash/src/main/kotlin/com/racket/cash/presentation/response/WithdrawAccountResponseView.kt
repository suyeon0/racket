package com.racket.cash.presentation.response

data class WithdrawAccountResponseView (

    val id: Long,
    val bankCode: Long,
    val accountNumber: String,
    val isPayable: Boolean

)