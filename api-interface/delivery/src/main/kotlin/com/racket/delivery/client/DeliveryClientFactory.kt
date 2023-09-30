package com.racket.delivery.client

import com.racket.delivery.client.feign.CJFeignClient
import com.racket.delivery.client.feign.HanjinFeignClient
import com.racket.delivery.enums.DeliveryCompanyType
import org.springframework.stereotype.Component

@Component
class DeliveryClientFactory(

    private val cjFeignClient: CJFeignClient,
    private val hanjinFeignClient: HanjinFeignClient


) {
    fun createDeliveryClient(invoiceNo: String, deliveryCompany: DeliveryCompanyType) =
        when (deliveryCompany) {
            DeliveryCompanyType.CJ -> CJDeliveryRequestClient(client = cjFeignClient)
            DeliveryCompanyType.HANJIN -> HanjinDeliveryRequestClient(client = hanjinFeignClient)
        }

}