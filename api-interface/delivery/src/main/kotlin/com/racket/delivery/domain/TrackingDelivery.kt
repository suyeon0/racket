package com.racket.delivery.domain

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.enums.DeliveryStatusType

data class TrackingDelivery (

    val id: Long,

    val deliveryCompany: DeliveryCompanyType,

    val invoiceNumber: String,

    val status: DeliveryStatusType,

    val driver: String,

    val timeLine: String
)