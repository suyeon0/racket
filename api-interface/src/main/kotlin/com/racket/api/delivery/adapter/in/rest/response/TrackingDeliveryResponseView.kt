package com.racket.api.delivery.adapter.`in`.rest.response

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.api.delivery.common.vo.TrackingVO

data class TrackingDeliveryResponseView(
    private val deliveryCompany: DeliveryCompanyType,
    private val invoiceNumber: String,
    private val timeLine: List<TrackingVO>
)