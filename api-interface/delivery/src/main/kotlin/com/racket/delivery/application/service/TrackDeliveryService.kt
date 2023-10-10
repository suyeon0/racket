package com.racket.delivery.application.service

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.adapter.out.kafka.DeliveryApiLogProducer
import com.racket.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.delivery.common.enums.DeliveryCompanyType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TrackDeliveryService(
    private val deliveryClientFactoryService: DeliveryClientFactoryService
): TrackDeliveryUseCase {

    override fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView {
        val client = this.deliveryClientFactoryService.getClient(deliveryCompanyType = deliveryCompany)
        return client.call(invoiceNo = invoiceNumber)
    }


}