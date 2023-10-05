package com.racket.delivery.adapter.`in`.rest.request

import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.exception.InvalidDeliveryCompanyTypeException

data class TrackDeliveryRequest(
    val deliveryCompanyType: DeliveryCompanyType,
    val invoiceNumber: String

) {

    fun validate() {
        if (DeliveryCompanyType.isInValidDeliveryCompany(deliveryCompanyType = deliveryCompanyType)) {
            throw InvalidDeliveryCompanyTypeException()
        }
    }

}