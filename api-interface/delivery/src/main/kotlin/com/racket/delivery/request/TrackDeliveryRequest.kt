package com.racket.delivery.request

import com.racket.delivery.enums.DeliveryCompanyType
import com.racket.delivery.exception.InvalidDeliveryCompanyTypeException

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