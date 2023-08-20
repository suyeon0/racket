package com.racket.api.payment.account.domain

import javax.persistence.*

@Entity
@Table(name = "account_payment")
class AccountPayment (

    @Id @GeneratedValue
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "bank_code", nullable = false)
    val bankCode: Long,

    @Column(name = "account_number", nullable = false)
    val accountNumber: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "is_payable", nullable = false)
    val isPayable: Boolean

)