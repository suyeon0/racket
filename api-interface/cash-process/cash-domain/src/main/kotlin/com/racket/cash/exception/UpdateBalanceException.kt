package com.racket.cash.exception

class UpdateBalanceException: RuntimeException() {
    override val message: String = "Update Balance Process Failed."
}