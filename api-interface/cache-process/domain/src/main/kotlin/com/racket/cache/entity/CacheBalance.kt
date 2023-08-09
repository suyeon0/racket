package com.racket.cache.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CacheBalance(

    @Id
    val userId: Long,

    var balance: Long

) {
    fun validateBalance() {
        if (this.balance < 0) {
            throw IllegalArgumentException("balance is must be greater than zero.")
        }
    }

    fun updateBalance (amount: Long): CacheBalance {
        this.balance += amount
        return this
    }
}