package com.racket.delivery.adapter.out.databases.mongo

import com.racket.delivery.common.enums.DeliveryCompanyType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

interface DeliveryApiLogMongoRepository : MongoRepository<DeliveryApiLogEntity, ObjectId> {
    fun findTop1ByCompanyTypeAndInvoiceNoAndResponseTimeBetween(
        companyType: DeliveryCompanyType,
        invoiceNo: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): DeliveryApiLogEntity?

}