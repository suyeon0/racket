package com.racket.delivery

import com.racket.delivery.enums.DeliveryCompanyType
import com.racket.delivery.response.OptionDeliveryDaysResponseView
import com.racket.delivery.response.TrackingDeliveryResponseView

interface DeliveryService {

    fun getDeliveryDaysByOption(optionId: Long): OptionDeliveryDaysResponseView


    fun trackDelivery(deliveryCompany: DeliveryCompanyType, invoiceNumber: String): TrackingDeliveryResponseView

}