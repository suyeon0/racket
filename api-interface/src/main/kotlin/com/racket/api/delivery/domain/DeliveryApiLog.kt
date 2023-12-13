package com.racket.api.delivery.domain

import com.racket.delivery.common.enums.DeliveryCompanyType
import java.time.LocalDateTime

data class DeliveryApiLog (
    val id: String? = null,
    val companyType: DeliveryCompanyType,
    val invoiceNo: String,
    val responseTime: LocalDateTime,
    val response: String
)