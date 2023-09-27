package com.racket.delivery.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "option_delivery_days")
class OptionDeliveryDays (

    @Id @GeneratedValue
    val id: Long = 0L,

    val optionId: Long,

    val days: Long
)