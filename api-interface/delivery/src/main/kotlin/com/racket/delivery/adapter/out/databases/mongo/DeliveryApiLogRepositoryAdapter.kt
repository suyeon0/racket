package com.racket.delivery.adapter.out.databases.mongo

import com.racket.delivery.application.port.out.databases.DeliveryApiLogRepositoryPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.domain.DeliveryApiLog
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class DeliveryApiLogRepositoryAdapter(
    private val deliveryApiLogMongoRepository: DeliveryApiLogMongoRepository
) : DeliveryApiLogRepositoryPort {

    override fun findTop1ByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): Optional<DeliveryApiLog> {
        val entity = this.deliveryApiLogMongoRepository.findTop1ByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
            companyType,
            invoiceNo,
            start,
            end
        )
        return Optional.ofNullable(entity).map { it.toDomain() }
    }
}