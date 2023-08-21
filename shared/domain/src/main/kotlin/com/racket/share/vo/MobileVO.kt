package com.racket.share.vo

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MobileVO(
    @Column(name = "mobile")
    val number: String
)