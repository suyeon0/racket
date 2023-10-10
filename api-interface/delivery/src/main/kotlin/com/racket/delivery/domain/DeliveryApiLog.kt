package com.racket.delivery.domain

import com.racket.delivery.common.enums.DeliveryCompanyType
import java.time.Instant

data class DeliveryApiLog (
    val id: String?,
    val companyType: DeliveryCompanyType,
    val invoiceNo: String,
    val responseTime: Instant,
    val response: String
)