package com.racket.cash.request

data class UpdateBalanceCommand (

    val userId :Long,
    val amount : Long

)