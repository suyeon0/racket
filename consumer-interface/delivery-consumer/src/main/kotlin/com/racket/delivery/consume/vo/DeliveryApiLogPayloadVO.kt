package com.racket.delivery.consume.vo

import com.racket.delivery.common.enums.DeliveryCompanyType
import java.time.Instant

class DeliveryApiLogPayloadVO (
    val companyType: DeliveryCompanyType,
    val invoiceNo: String,
    val responseTime: Instant,
    val response: String
)