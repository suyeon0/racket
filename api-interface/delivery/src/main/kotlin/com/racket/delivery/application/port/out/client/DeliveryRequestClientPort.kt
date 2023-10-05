package com.racket.delivery.application.port.out.client

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView

interface DeliveryRequestClientPort {
    fun call(invoiceNo: String): TrackingDeliveryResponseView
}