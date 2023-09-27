package com.racket.delivery.client

import org.springframework.stereotype.Component

@Component
class DeliveryClientFactory {
    fun createDeliveryClient(invoiceNo: String, company: String): DeliveryRequestClient =
        when(company) {
            "CJ" -> CJDeliveryRequestClient()
            else -> HanjinDeliveryRequestClient()
        }
}