package com.racket.delivery.application.port.out.databases

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.domain.DeliveryApiLog
import java.time.Instant
import java.util.*

interface DeliveryApiLogRepositoryPort {

    fun findByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: Long,
        start: Instant,
        end: Instant
    ): List<DeliveryApiLog>

    fun save(entity: DeliveryApiLog): Optional<DeliveryApiLog>

}