package com.racket.api.shared.vo

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MobileVO(
    @Column(name = "mobile")
    val number: String
)