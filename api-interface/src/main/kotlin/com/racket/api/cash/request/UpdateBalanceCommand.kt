package com.racket.api.cash.request

data class UpdateBalanceCommand (

    val userId :Long,
    val amount : Long

)