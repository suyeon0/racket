package com.racket.cash.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CashBalance(

    @Id
    val userId: Long,

    var balance: Long

) {
    fun validateBalance() {
        if (this.balance < 0) {
            throw IllegalArgumentException("balance is must be greater than zero.")
        }
    }

    fun updateBalance (amount: Long): CashBalance {
        this.balance += amount
        return this
    }
}