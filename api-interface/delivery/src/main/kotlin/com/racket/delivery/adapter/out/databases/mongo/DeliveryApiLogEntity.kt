package com.racket.delivery.adapter.out.databases.mongo

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.domain.DeliveryApiLog
import org.bson.json.JsonObject
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Id

@Document("deliveryApiLog")
class DeliveryApiLogEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: ObjectId? = null,

    @Column(name = "company_type", nullable = false)
    var companyType: DeliveryCompanyType,

    @Column(name = "invoice_no", nullable = false)
    val invoiceNo: String,

    @Column(name = "response_at", nullable = false)
    val responseTime: LocalDateTime,

    @Column(name = "response", nullable = true)
    val response: JsonObject

) {

    companion object {
        fun of(deliveryApiLog: DeliveryApiLog) = DeliveryApiLogEntity(
            companyType = deliveryApiLog.companyType,
            invoiceNo = deliveryApiLog.invoiceNo,
            response = JsonObject(deliveryApiLog.response),
            responseTime = deliveryApiLog.responseTime
        )
    }

    fun toDomain(): DeliveryApiLog = DeliveryApiLog(
        id = id.toString(),
        companyType = companyType,
        invoiceNo = invoiceNo,
        responseTime = responseTime,
        response = response.toString()
    )

}