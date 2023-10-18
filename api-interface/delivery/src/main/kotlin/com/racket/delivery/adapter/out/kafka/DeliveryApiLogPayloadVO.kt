package com.racket.delivery.adapter.out.kafka

import com.racket.delivery.common.enums.DeliveryCompanyType
import java.time.Instant

data class DeliveryApiLogPayloadVO (
    val companyType: DeliveryCompanyType,
    val invoiceNo: String,
    val responseTime: Instant,
    val response: String
)