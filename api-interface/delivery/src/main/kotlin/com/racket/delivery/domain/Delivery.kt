package com.racket.delivery.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Delivery (

    @Id @GeneratedValue
    val id: Long,

   // val deliveryCompany: DeliveryCompanyType,

    val invoiceNumber: String,

    //val status: DeliveryStatusType,

    val driver: String,

    val timeLine: String
)