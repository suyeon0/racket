package com.racket.cash.consume.const

class DeadLetterType {

    companion object {
        const val INSERT_DB = "CASH-FAIL-DB"
        const val RETRY = "CASH-RETRY"
    }

}