package com.racket.delivery.adapter.out.client.feign.hanjin

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.adapter.out.kafka.DeliveryApiLogProducer
import com.racket.delivery.application.port.out.client.DeliveryRequestClientPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.domain.DeliveryApiLog
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class HanjinClientAdapter(
    private val client: HanjinDeliveryApiFeignClient,
    private val deliveryApiLogProducer: DeliveryApiLogProducer
): DeliveryRequestClientPort {

    @Transactional
    override fun call(invoiceNo: String): TrackingDeliveryResponseView {
        val response =
            this.client.getTrackingDeliveryList(request = HanjinDeliveryApiRequest(invoiceNo = invoiceNo)).body as HanjinDeliveryApiResponse
        return response.toCommonView()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun callLogProducer(response: HanjinDeliveryApiResponse) {
        val record = DeliveryApiLog(
            id = null,
            companyType = DeliveryCompanyType.HANJIN,
            invoiceNo = response.invoice,
            responseTime = Instant.now(),
            response = response.toString()
        )
        this.deliveryApiLogProducer.produce(record)
    }

}