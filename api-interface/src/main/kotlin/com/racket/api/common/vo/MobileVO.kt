package com.racket.api.common.vo

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MobileVO(
    @Column(name = "mobile")
    val number: String
)