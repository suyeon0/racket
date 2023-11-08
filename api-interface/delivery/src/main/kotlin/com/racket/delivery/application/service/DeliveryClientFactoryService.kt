package com.racket.delivery.application.service

import com.racket.delivery.application.port.out.client.DeliveryRequestClientPort
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.vo.TrackingVO
import org.springframework.stereotype.Component

@Component
class DeliveryClientFactoryService(
    private val cjClientAdapter: DeliveryRequestClientPort,
    private val hanjinClientAdapter: DeliveryRequestClientPort
) {

    fun getClientAdapterByDeliveryCompanyType(deliveryCompanyType: DeliveryCompanyType) =
        when (deliveryCompanyType) {
            DeliveryCompanyType.CJ -> cjClientAdapter
            DeliveryCompanyType.HANJIN -> hanjinClientAdapter
        }

}