package com.racket.cache.presentation.request

data class UpdateBalanceCommand (

    val userId :Long,
    val amount : Long

) {
    fun validate() {
        if (amount < 100000) throw IllegalArgumentException("amount is invalid.")
    }
}