package com.racket.delivery.client

import com.racket.delivery.vo.DeliveryResponseVO

class CJDeliveryRequestClient(
   // private val cjClient: CjClient

): DeliveryRequestClient {
    override fun call(): DeliveryResponseVO {
//        val response = cjClient.call()
//        response -> DeliveryResponseVO
//        return  DeliveryResponseVO()
        return DeliveryResponseVO(invoiceNumber = "", driver = "", timeLine = "")
    }
}