package com.racket.cash.presentation.request

data class UpdateBalanceCommand (

    val userId :Long,
    val amount : Long

)