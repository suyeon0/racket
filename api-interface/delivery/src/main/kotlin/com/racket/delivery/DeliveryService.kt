package com.racket.delivery

import com.racket.delivery.response.OptionDeliveryDaysResponseView
import com.racket.delivery.vo.DeliveryResponseVO

interface DeliveryService {

    fun getDeliveryDaysByOption(optionId: Long): OptionDeliveryDaysResponseView


    fun getDeliveryInformation(invoiceNumber: String, deliveryCompany: String): DeliveryResponseVO

}