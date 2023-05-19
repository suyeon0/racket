package com.racket.api.common.vo

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Address(

    @Column(name = "street")
    val street: String?,

    @Column(name = "zip_code")
    val zipCode: String?,

    @Column(name = "detailed_address")
    val detailedAddress: String?

)