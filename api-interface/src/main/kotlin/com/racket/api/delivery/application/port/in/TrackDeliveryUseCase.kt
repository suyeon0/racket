package com.racket.api.delivery.application.port.`in`

import com.racket.api.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.common.enums.DeliveryCompanyType

interface TrackDeliveryUseCase {

    /**
     * 실시간 배송 조회
     */
    fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView
}