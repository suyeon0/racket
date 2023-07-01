package com.racket.api.shared.vo

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AddressVO(

    @Column(name = "street")
    val street: String?,

    @Column(name = "zip_code")
    val zipCode: String?,

    @Column(name = "detailed_address")
    val detailedAddress: String?

)