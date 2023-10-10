package com.racket.delivery.adapter.out.databases.mongo

import com.racket.delivery.application.port.out.databases.DeliveryApiLogRepositoryPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.domain.DeliveryApiLog
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class DeliveryApiLogRepositoryAdapter(
    private val deliveryApiLogMongoRepository: DeliveryApiLogMongoRepository
) : DeliveryApiLogRepositoryPort {
    override fun findByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: Long,
        start: Instant,
        end: Instant
    ): List<DeliveryApiLog> {
        val entityList = this.deliveryApiLogMongoRepository.findByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
            companyType,
            invoiceNo,
            start,
            end
        )
        return entityList.stream().map { it.toDomain() }.toList()
    }

    override fun save(log: DeliveryApiLog): Optional<DeliveryApiLog> {
        val result = this.deliveryApiLogMongoRepository.save(DeliveryApiLogEntity.of(log))
        return Optional.of(result.toDomain())
    }

}