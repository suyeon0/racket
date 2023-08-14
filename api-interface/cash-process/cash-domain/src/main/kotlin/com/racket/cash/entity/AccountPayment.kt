package com.racket.cash.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class AccountPayment (

    @Id @GeneratedValue
    val id: Long,

    val bankCode: Long,

    val accountNumber: String,

    val userId: Long,

    val isPayable: Boolean

)