package com.racket.api.delivery.adapter.`in`.rest.request

import com.racket.delivery.common.enums.DeliveryCompanyType

data class TrackDeliveryRequest(
    private val deliveryCompanyType: DeliveryCompanyType,
    private val invoiceNumber: String
)