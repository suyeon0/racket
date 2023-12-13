package com.racket.api.delivery.application.port.out.databases

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.api.delivery.domain.DeliveryApiLog
import java.time.LocalDateTime
import java.util.*

interface DeliveryApiLogRepositoryPort {

    fun findTop1ByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): Optional<DeliveryApiLog>

}