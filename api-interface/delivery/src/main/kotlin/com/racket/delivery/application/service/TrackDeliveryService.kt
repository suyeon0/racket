package com.racket.delivery.application.service

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.application.port.`in`.TrackDeliveryUseCase
import com.racket.delivery.common.enums.DeliveryCompanyType
import org.springframework.stereotype.Service

@Service
class TrackDeliveryService(
    private val deliveryClientFactoryService: DeliveryClientFactoryService
): TrackDeliveryUseCase {
    override fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView {
        val client = this.deliveryClientFactoryService.getClient(deliveryCompanyType = deliveryCompany)
        return client.call(invoiceNo = invoiceNumber)
    }
}