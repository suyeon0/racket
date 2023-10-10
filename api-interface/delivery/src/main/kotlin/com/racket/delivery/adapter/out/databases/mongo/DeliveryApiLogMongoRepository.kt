package com.racket.delivery.adapter.out.databases.mongo

import com.racket.delivery.common.enums.DeliveryCompanyType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface DeliveryApiLogMongoRepository : MongoRepository<DeliveryApiLogEntity, ObjectId> {
    fun findByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: Long,
        start: Instant,
        end: Instant
    ): List<DeliveryApiLogEntity>

}