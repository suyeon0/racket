package com.racket.api.cache.domain.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CacheBalance(

    @Id
    val userId: Long,

    val balance: Long

) {
    fun validateBalance() {
        if (this.balance < 0) {
            throw IllegalArgumentException("balance is must be greater than zero.")
        }
    }
}