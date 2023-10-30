package com.racket.delivery.adapter.`in`.rest.response

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.vo.TrackingVO

data class TrackingDeliveryResponseView(

    val deliveryCompany: DeliveryCompanyType,

    val invoiceNumber: String,

    val timeLine: List<TrackingVO>
)