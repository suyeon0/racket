package com.racket.delivery.response

import com.racket.delivery.enums.DeliveryCompanyType
import com.racket.delivery.vo.TrackingVO

data class TrackingDeliveryResponseView (


    val deliveryCompany: DeliveryCompanyType,

    val invoiceNumber: String,

    val driver: String,

    val timeLine: List<TrackingVO>

)