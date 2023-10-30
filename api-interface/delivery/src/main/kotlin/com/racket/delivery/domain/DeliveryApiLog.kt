package com.racket.delivery.domain

import com.racket.delivery.common.enums.DeliveryCompanyType
import java.time.LocalDateTime

data class DeliveryApiLog (
    val id: String,
    val companyType: DeliveryCompanyType,
    val invoiceNo: String,
    val responseTime: LocalDateTime,
    val response: String
)