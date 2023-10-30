package com.racket.delivery.adapter.`in`.rest.request

import com.racket.delivery.common.enums.DeliveryCompanyType

data class TrackDeliveryRequest(
    val deliveryCompanyType: DeliveryCompanyType,
    val invoiceNumber: String
)