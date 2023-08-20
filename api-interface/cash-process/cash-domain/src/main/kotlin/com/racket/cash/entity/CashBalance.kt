package com.racket.cash.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "cash_balance")
class CashBalance(

    @Id
    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "balance")
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