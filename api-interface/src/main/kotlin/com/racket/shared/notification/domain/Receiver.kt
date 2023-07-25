package com.racket.shared.notification.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Receiver(

    @Column(nullable = false)
    private var userId: Long

)