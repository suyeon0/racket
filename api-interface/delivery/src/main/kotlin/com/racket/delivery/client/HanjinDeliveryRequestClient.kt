package com.racket.delivery.client

import com.racket.delivery.vo.DeliveryResponseVO

class HanjinDeliveryRequestClient: DeliveryRequestClient {
    override fun call(): DeliveryResponseVO {
        return DeliveryResponseVO(invoiceNumber = "", driver = "", timeLine = "")
    }
}