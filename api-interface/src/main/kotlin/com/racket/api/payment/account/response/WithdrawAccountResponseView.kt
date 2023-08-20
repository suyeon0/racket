package com.racket.api.payment.account.response

data class WithdrawAccountResponseView (

    val id: Long,
    val bankCode: Long,
    val accountNumber: String,
    val isPayable: Boolean

)