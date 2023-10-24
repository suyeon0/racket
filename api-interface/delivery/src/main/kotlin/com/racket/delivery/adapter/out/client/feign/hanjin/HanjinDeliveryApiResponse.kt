package com.racket.delivery.adapter.out.client.feign.hanjin

import com.racket.delivery.adapter.`in`.rest.response.TrackingDeliveryResponseView
import com.racket.delivery.common.enums.DeliveryCompanyType
import com.racket.delivery.common.enums.DeliveryStatusType
import com.racket.delivery.common.vo.TrackingVO
import java.time.Instant

data class HanjinDeliveryApiResponse(

    val company: String,
    val invoice: String,
    val driver: String,
    val timeLine: List<HanjinTrackingVO>

)
fun HanjinDeliveryApiResponse.toCommonView() =
    TrackingDeliveryResponseView(
        deliveryCompany = DeliveryCompanyType.HANJIN,
        invoiceNumber = invoice,
        driver = driver,
        timeLine = timeLine.stream().map{it.toCommonVO()}.toList()
    )

data class HanjinTrackingVO (

    val timestamp: Instant, // 처리일시
    val currentLocation: String, // 현재위치
    val deliveryStatus: DeliveryStatusType // 배송상태

)
fun HanjinTrackingVO.toCommonVO() =
    TrackingVO(
        timestamp = timestamp,
        currentLocation = currentLocation,
        deliveryStatus = deliveryStatus
    )
