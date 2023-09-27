package com.racket.delivery.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "delivery_information")
class Delivery {

    @Id @GeneratedValue
    val id: Long = 0L
}