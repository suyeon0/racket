package com.racket.api.delivery.application.port.out.client

import com.racket.api.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView

interface DeliveryRequestClientPort {
    fun call(invoiceNo: String): TrackingDeliveryResponseView
}